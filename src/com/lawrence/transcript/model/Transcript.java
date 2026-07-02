package com.lawrence.transcript.model;

import com.lawrence.transcript.service.ClassificationService;

import java.time.LocalDate;

public class Transcript {
    private final Student student;
    private final LocalDate generatedOn;
    private final ClassificationService classificationService = new ClassificationService();

    public Transcript(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student is required.");
        }
        this.student = student;
        this.generatedOn = LocalDate.now();
    }

    public Student getStudent() {
        return student;
    }

    public LocalDate getGeneratedOn() {
        return generatedOn;
    }

    public double getCgpa() {
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        for (SemesterRecord semester : student.getSemesters()) {
            totalQualityPoints += semester.getTotalQualityPoints();
            totalCredits += semester.getTotalCredits();
        }
        if (totalCredits == 0) {
            return 0.0;
        }
        return totalQualityPoints / totalCredits;
    }

    public String getClassification() {
        return classificationService.classify(getCgpa());
    }
}
