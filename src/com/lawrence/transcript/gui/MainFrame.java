package com.lawrence.transcript.gui;

import com.lawrence.transcript.exception.InvalidInputException;
import com.lawrence.transcript.formatter.OfficialTranscriptFormatter;
import com.lawrence.transcript.formatter.TranscriptFormatter;
import com.lawrence.transcript.formatter.UnofficialTranscriptFormatter;
import com.lawrence.transcript.model.CoreCourse;
import com.lawrence.transcript.model.Course;
import com.lawrence.transcript.model.CourseResult;
import com.lawrence.transcript.model.ElectiveCourse;
import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Student;
import com.lawrence.transcript.model.Transcript;
import com.lawrence.transcript.service.AuthService;
import com.lawrence.transcript.service.StudentRepository;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MainFrame extends JFrame {
    private static final String CARD_LOGIN = "login";
    private static final String CARD_STUDENTS = "students";
    private static final String CARD_RESULTS = "results";
    private static final String CARD_TRANSCRIPT = "transcript";

    private final AuthService authService = new AuthService();
    private final StudentRepository studentRepository = new StudentRepository();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final DefaultListModel<Student> studentListModel = new DefaultListModel<>();
    private final JList<Student> studentList = new JList<>(studentListModel);
    private final DefaultTableModel resultTableModel = new DefaultTableModel(
            new String[]{"Semester", "Code", "Course", "Category", "Credits", "Score", "Grade", "Quality"}, 0);
    private final JTable resultTable = new JTable(resultTableModel);
    private final JTextArea transcriptArea = new JTextArea();
    private final JComboBox<String> formatterChoice = new JComboBox<>(new String[]{"Official", "Unofficial"});
    private final JLabel selectedStudentLabel = new JLabel("No student selected");
    private Student currentStudent;

    public MainFrame() {
        super("Academic Transcript Generation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 650));
        setSize(980, 650);
        setLocationRelativeTo(null);

        cards.add(buildLoginPanel(), CARD_LOGIN);
        cards.add(buildStudentPanel(), CARD_STUDENTS);
        cards.add(buildResultsPanel(), CARD_RESULTS);
        cards.add(buildTranscriptPanel(), CARD_TRANSCRIPT);
        add(cards);

        refreshStudentList();
        if (!studentRepository.getStudents().isEmpty()) {
            currentStudent = studentRepository.getStudents().get(0);
            studentList.setSelectedValue(currentStudent, true);
        }
        cardLayout.show(cards, CARD_LOGIN);
    }

    private JPanel buildLoginPanel() {
        JPanel page = pagePanel();
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(214, 221, 230)),
                BorderFactory.createEmptyBorder(28, 34, 28, 34)));

        JTextField usernameField = new JTextField("registrar", 18);
        JTextField pinField = new JTextField("1234", 18);
        JButton loginButton = new JButton("Login");
        AppTheme.styleButton(loginButton);

        addFormRow(form, 0, AppTheme.title("Registrar Login"), null);
        addFormRow(form, 1, new JLabel("Username"), usernameField);
        addFormRow(form, 2, new JLabel("PIN"), pinField);
        addFormRow(form, 3, new JLabel(""), loginButton);

        loginButton.addActionListener(event -> {
            try {
                authService.login(usernameField.getText(), pinField.getText());
                cardLayout.show(cards, CARD_STUDENTS);
            } catch (InvalidInputException ex) {
                showError(ex.getMessage());
            }
        });

        page.add(form, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildStudentPanel() {
        JPanel page = pagePanel();
        JPanel header = header("Students", "Choose a student or add a new record");
        page.add(header, BorderLayout.NORTH);

        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.setFont(AppTheme.BODY_FONT);
        studentList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                currentStudent = studentList.getSelectedValue();
                updateSelectedStudentLabel();
                refreshResultsTable();
            }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton addStudentButton = new JButton("Add Student");
        JButton addResultsButton = new JButton("Add Results");
        JButton transcriptButton = new JButton("View Transcript");
        AppTheme.styleSecondaryButton(addStudentButton);
        AppTheme.styleButton(addResultsButton);
        AppTheme.styleButton(transcriptButton);
        actions.add(addStudentButton);
        actions.add(addResultsButton);
        actions.add(transcriptButton);

        addStudentButton.addActionListener(event -> showAddStudentDialog());
        addResultsButton.addActionListener(event -> {
            if (requireStudent()) {
                refreshResultsTable();
                cardLayout.show(cards, CARD_RESULTS);
            }
        });
        transcriptButton.addActionListener(event -> {
            if (requireStudent()) {
                renderTranscript();
                cardLayout.show(cards, CARD_TRANSCRIPT);
            }
        });

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(new JScrollPane(studentList), BorderLayout.CENTER);
        center.add(actions, BorderLayout.SOUTH);
        AppTheme.pad(center);
        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildResultsPanel() {
        JPanel page = pagePanel();
        page.add(header("Add Course Results", "Enter raw score; the system maps it to grade points"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        JTextField semesterField = new JTextField("2025/2026 Semester 1", 18);
        JTextField codeField = new JTextField(12);
        JTextField titleField = new JTextField(20);
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 12, 1));
        JSpinner scoreSpinner = new JSpinner(new SpinnerNumberModel(80, 0, 100, 1));
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Core", "Elective"});
        JButton addButton = new JButton("Add Result");
        AppTheme.styleButton(addButton);

        addFormRow(form, 0, new JLabel("Selected Student"), selectedStudentLabel);
        addFormRow(form, 1, new JLabel("Semester"), semesterField);
        addFormRow(form, 2, new JLabel("Course Code"), codeField);
        addFormRow(form, 3, new JLabel("Course Title"), titleField);
        addFormRow(form, 4, new JLabel("Credit Hours"), creditSpinner);
        addFormRow(form, 5, new JLabel("Score"), scoreSpinner);
        addFormRow(form, 6, new JLabel("Category"), categoryBox);
        addFormRow(form, 7, new JLabel(""), addButton);

        addButton.addActionListener(event -> {
            if (!requireStudent()) {
                return;
            }
            try {
                String category = String.valueOf(categoryBox.getSelectedItem());
                Course course = "Elective".equals(category)
                        ? new ElectiveCourse(codeField.getText(), titleField.getText(), (Integer) creditSpinner.getValue())
                        : new CoreCourse(codeField.getText(), titleField.getText(), (Integer) creditSpinner.getValue());
                CourseResult result = new CourseResult(course, (Integer) scoreSpinner.getValue());
                SemesterRecord semester = currentStudent.findOrCreateSemester(semesterField.getText());
                semester.addResult(result);
                codeField.setText("");
                titleField.setText("");
                refreshResultsTable();
                JOptionPane.showMessageDialog(this, "Course result added.");
            } catch (InvalidInputException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton backButton = new JButton("Back");
        JButton transcriptButton = new JButton("View Transcript");
        AppTheme.styleSecondaryButton(backButton);
        AppTheme.styleButton(transcriptButton);
        backButton.addActionListener(event -> cardLayout.show(cards, CARD_STUDENTS));
        transcriptButton.addActionListener(event -> {
            renderTranscript();
            cardLayout.show(cards, CARD_TRANSCRIPT);
        });
        actions.add(backButton);
        actions.add(transcriptButton);

        JPanel content = new JPanel(new BorderLayout(18, 12));
        content.setOpaque(false);
        content.add(form, BorderLayout.WEST);
        content.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        content.add(actions, BorderLayout.SOUTH);
        AppTheme.pad(content);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildTranscriptPanel() {
        JPanel page = pagePanel();
        page.add(header("Transcript View", "Review, print, or export the generated transcript"), BorderLayout.NORTH);

        transcriptArea.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 13));
        transcriptArea.setEditable(false);
        transcriptArea.setLineWrap(false);

        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topActions.setOpaque(false);
        topActions.add(new JLabel("Format"));
        topActions.add(formatterChoice);
        formatterChoice.addActionListener(event -> renderTranscript());

        JPanel bottomActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomActions.setOpaque(false);
        JButton backButton = new JButton("Back");
        JButton printButton = new JButton("Print");
        JButton exportButton = new JButton("Export Text");
        AppTheme.styleSecondaryButton(backButton);
        AppTheme.styleButton(printButton);
        AppTheme.styleButton(exportButton);
        bottomActions.add(backButton);
        bottomActions.add(printButton);
        bottomActions.add(exportButton);

        backButton.addActionListener(event -> cardLayout.show(cards, CARD_RESULTS));
        printButton.addActionListener(event -> printTranscript());
        exportButton.addActionListener(event -> exportTranscript());

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(topActions, BorderLayout.NORTH);
        content.add(new JScrollPane(transcriptArea), BorderLayout.CENTER);
        content.add(bottomActions, BorderLayout.SOUTH);
        AppTheme.pad(content);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private void showAddStudentDialog() {
        JTextField idField = new JTextField(18);
        JTextField nameField = new JTextField(18);
        JTextField programmeField = new JTextField("MSc Information Technology Education", 18);
        JPanel panel = new JPanel(new GridBagLayout());
        addFormRow(panel, 0, new JLabel("Student ID"), idField);
        addFormRow(panel, 1, new JLabel("Name"), nameField);
        addFormRow(panel, 2, new JLabel("Programme"), programmeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Student student = new Student(idField.getText(), nameField.getText(), programmeField.getText());
                studentRepository.add(student);
                refreshStudentList();
                studentList.setSelectedValue(student, true);
            } catch (InvalidInputException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void renderTranscript() {
        if (currentStudent == null) {
            transcriptArea.setText("No student selected.");
            return;
        }
        Transcript transcript = new Transcript(currentStudent);
        TranscriptFormatter formatter = "Unofficial".equals(formatterChoice.getSelectedItem())
                ? new UnofficialTranscriptFormatter()
                : new OfficialTranscriptFormatter();
        transcriptArea.setText(formatter.format(transcript));
        transcriptArea.setCaretPosition(0);
    }

    private void refreshStudentList() {
        studentListModel.clear();
        for (Student student : studentRepository.getStudents()) {
            studentListModel.addElement(student);
        }
    }

    private void refreshResultsTable() {
        resultTableModel.setRowCount(0);
        updateSelectedStudentLabel();
        if (currentStudent == null) {
            return;
        }
        for (SemesterRecord semester : currentStudent.getSemesters()) {
            List<CourseResult> results = semester.getResults();
            for (CourseResult result : results) {
                resultTableModel.addRow(new Object[]{
                        semester.getSemesterName(),
                        result.getCourse().getCode(),
                        result.getCourse().getTitle(),
                        result.getCourse().getCategory(),
                        result.getCreditHours(),
                        result.getScore(),
                        result.getGrade().getLabel(),
                        String.format("%.2f", result.getQualityPoints())
                });
            }
            resultTableModel.addRow(new Object[]{
                    semester.getSemesterName(),
                    "",
                    "Semester GPA",
                    "",
                    semester.getTotalCredits(),
                    "",
                    "",
                    String.format("%.2f", semester.getGpa())
            });
        }
    }

    private boolean requireStudent() {
        if (currentStudent == null) {
            showError("Please select or add a student first.");
            return false;
        }
        return true;
    }

    private void updateSelectedStudentLabel() {
        if (currentStudent == null) {
            selectedStudentLabel.setText("No student selected");
        } else {
            selectedStudentLabel.setText(currentStudent.getId() + " - " + currentStudent.getName());
        }
    }

    private void printTranscript() {
        try {
            transcriptArea.print();
        } catch (PrinterException ex) {
            showError("Printing failed: " + ex.getMessage());
        }
    }

    private void exportTranscript() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("transcript.txt"));
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(chooser.getSelectedFile().toPath(), transcriptArea.getText());
                JOptionPane.showMessageDialog(this, "Transcript exported.");
            } catch (IOException ex) {
                showError("Could not export transcript: " + ex.getMessage());
            }
        }
    }

    private JPanel pagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        return panel;
    }

    private JPanel header(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 24, 6, 24));
        JLabel titleLabel = AppTheme.title(title);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(AppTheme.BODY_FONT);
        subtitleLabel.setForeground(AppTheme.MUTED);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void addFormRow(JPanel panel, int row, Component label, Component input) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(7, 7, 7, 12);
        if (label instanceof JLabel) {
            ((JLabel) label).setHorizontalAlignment(SwingConstants.LEFT);
        }
        panel.add(label, labelConstraints);

        if (input != null) {
            GridBagConstraints inputConstraints = new GridBagConstraints();
            inputConstraints.gridx = 1;
            inputConstraints.gridy = row;
            inputConstraints.weightx = 1.0;
            inputConstraints.fill = GridBagConstraints.HORIZONTAL;
            inputConstraints.insets = new Insets(7, 7, 7, 7);
            panel.add(input, inputConstraints);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}


