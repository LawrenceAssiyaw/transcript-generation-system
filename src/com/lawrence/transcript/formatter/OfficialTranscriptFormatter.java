package com.lawrence.transcript.formatter;

import com.lawrence.transcript.model.CourseResult;
import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Student;
import com.lawrence.transcript.model.Transcript;

public class OfficialTranscriptFormatter implements TranscriptFormatter {
    @Override
    public String format(Transcript transcript) {
        Student student = transcript.getStudent();
        StringBuilder builder = new StringBuilder();
        builder.append("ACADEMIC TRANSCRIPT\n");
        builder.append("Generated On: ").append(transcript.getGeneratedOn()).append("\n\n");
        builder.append("Student ID : ").append(student.getId()).append("\n");
        builder.append("Name       : ").append(student.getName()).append("\n");
        builder.append("Programme  : ").append(student.getProgramme()).append("\n");
        builder.append("------------------------------------------------------------\n");
        builder.append(String.format("%-10s %-28s %7s %7s %8s%n", "Code", "Course", "Credit", "Grade", "Quality"));
        builder.append("------------------------------------------------------------\n");

        for (SemesterRecord semester : student.getSemesters()) {
            builder.append("\n").append(semester.getSemesterName()).append("\n");
            for (CourseResult result : semester.getResults()) {
                builder.append(String.format(
                        "%-10s %-28s %7d %7s %8.2f%n",
                        result.getCourse().getCode(),
                        shorten(result.getCourse().getTitle(), 28),
                        result.getCreditHours(),
                        result.getGrade().getLabel(),
                        result.getQualityPoints()));
            }
            builder.append(String.format("Semester GPA: %.2f%n", semester.getGpa()));
        }

        builder.append("\n------------------------------------------------------------\n");
        builder.append(String.format("CGPA: %.2f%n", transcript.getCgpa()));
        builder.append("Classification: ").append(transcript.getClassification()).append("\n");
        return builder.toString();
    }

    private String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
