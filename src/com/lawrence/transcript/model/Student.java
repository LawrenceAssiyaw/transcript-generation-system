package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Student {
    private final String id;
    private final String name;
    private final String programme;
    private final List<SemesterRecord> semesters = new ArrayList<>();

    public Student(String id, String name, String programme) throws InvalidInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("Student ID cannot be blank.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Student name cannot be blank.");
        }
        if (programme == null || programme.trim().isEmpty()) {
            throw new InvalidInputException("Programme cannot be blank.");
        }

        this.id = id.trim().toUpperCase();
        this.name = name.trim();
        this.programme = programme.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProgramme() {
        return programme;
    }

    public List<SemesterRecord> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    public void addSemester(SemesterRecord semester) throws InvalidInputException {
        if (semester == null) {
            throw new InvalidInputException("Semester record is required.");
        }
        semesters.add(semester);
    }

    public SemesterRecord findOrCreateSemester(String semesterName) throws InvalidInputException {
        Optional<SemesterRecord> existing = semesters.stream()
                .filter(s -> s.getSemesterName().equalsIgnoreCase(semesterName.trim()))
                .findFirst();
        if (existing.isPresent()) {
            return existing.get();
        }
        SemesterRecord record = new SemesterRecord(semesterName);
        addSemester(record);
        return record;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
