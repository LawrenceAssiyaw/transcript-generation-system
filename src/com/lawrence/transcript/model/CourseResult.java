package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

public class CourseResult {
    private final Course course;
    private final Grade grade;
    private final int score;

    public CourseResult(Course course, int score) throws InvalidInputException {
        if (course == null) {
            throw new InvalidInputException("Course is required.");
        }
        this.course = course;
        this.score = score;
        this.grade = Grade.fromScore(score);
    }

    public Course getCourse() {
        return course;
    }

    public Grade getGrade() {
        return grade;
    }

    public int getScore() {
        return score;
    }

    public double getQualityPoints() {
        return grade.getPoints() * course.getCreditHours();
    }

    public int getCreditHours() {
        return course.getCreditHours();
    }
}
