package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

public enum Grade {
    A("A", 4.0),
    B_PLUS("B+", 3.5),
    B("B", 3.0),
    C_PLUS("C+", 2.5),
    C("C", 2.0),
    D("D", 1.0),
    F("F", 0.0);

    private final String label;
    private final double points;

    Grade(String label, double points) {
        this.label = label;
        this.points = points;
    }

    public String getLabel() {
        return label;
    }

    public double getPoints() {
        return points;
    }

    public static Grade fromScore(int score) throws InvalidInputException {
        if (score < 0 || score > 100) {
            throw new InvalidInputException("Score must be between 0 and 100.");
        }
        if (score >= 80) {
            return A;
        }
        if (score >= 70) {
            return B_PLUS;
        }
        if (score >= 60) {
            return B;
        }
        if (score >= 55) {
            return C_PLUS;
        }
        if (score >= 50) {
            return C;
        }
        if (score >= 40) {
            return D;
        }
        return F;
    }

    @Override
    public String toString() {
        return label + " (" + points + ")";
    }
}
