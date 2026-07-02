package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemesterRecord {
    private final String semesterName;
    private final List<CourseResult> results = new ArrayList<>();

    public SemesterRecord(String semesterName) throws InvalidInputException {
        if (semesterName == null || semesterName.trim().isEmpty()) {
            throw new InvalidInputException("Semester name cannot be blank.");
        }
        this.semesterName = semesterName.trim();
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void addResult(CourseResult result) throws InvalidInputException {
        if (result == null) {
            throw new InvalidInputException("Course result is required.");
        }
        results.add(result);
    }

    public List<CourseResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    public double getGpa() {
        int totalCredits = getTotalCredits();
        if (totalCredits == 0) {
            return 0.0;
        }
        return getTotalQualityPoints() / totalCredits;
    }

    public int getTotalCredits() {
        int total = 0;
        for (CourseResult result : results) {
            total += result.getCreditHours();
        }
        return total;
    }

    public double getTotalQualityPoints() {
        double total = 0.0;
        for (CourseResult result : results) {
            total += result.getQualityPoints();
        }
        return total;
    }

    @Override
    public String toString() {
        return semesterName;
    }
}
