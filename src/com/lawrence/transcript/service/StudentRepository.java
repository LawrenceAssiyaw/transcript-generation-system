package com.lawrence.transcript.service;

import com.lawrence.transcript.exception.InvalidInputException;
import com.lawrence.transcript.model.CoreCourse;
import com.lawrence.transcript.model.CourseResult;
import com.lawrence.transcript.model.ElectiveCourse;
import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    private final List<Student> students = new ArrayList<>();

    public StudentRepository() {
        seedStudents();
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public Optional<Student> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return students.stream()
                .filter(student -> student.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    public void add(Student student) throws InvalidInputException {
        if (student == null) {
            throw new InvalidInputException("Student is required.");
        }
        if (findById(student.getId()).isPresent()) {
            throw new InvalidInputException("A student with this ID already exists.");
        }
        students.add(student);
    }

    private void seedStudents() {
        try {
            Student lawrence = new Student("MS/ITE/25/0052", "Assiyaw Lawrence", "MSc Information Technology Education");
            SemesterRecord semesterOne = new SemesterRecord("2025/2026 Semester 1");
            semesterOne.addResult(new CourseResult(new CoreCourse("INF811D", "Object Oriented Programming", 3), 85));
            semesterOne.addResult(new CourseResult(new CoreCourse("INF812D", "Database Systems", 3), 68));
            semesterOne.addResult(new CourseResult(new ElectiveCourse("INF813D", "Educational Technology", 2), 72));
            lawrence.addSemester(semesterOne);
            students.add(lawrence);

            Student demo = new Student("MS/ITE/25/0001", "Demo Student", "MSc Information Technology Education");
            students.add(demo);
        } catch (InvalidInputException ex) {
            throw new IllegalStateException("Failed to seed demo data.", ex);
        }
    }
}
