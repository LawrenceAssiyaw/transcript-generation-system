package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

public abstract class Course {
    private final String code;
    private final String title;
    private final int creditHours;

    protected Course(String code, String title, int creditHours) throws InvalidInputException {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidInputException("Course code cannot be blank.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Course title cannot be blank.");
        }
        if (creditHours <= 0) {
            throw new InvalidInputException("Credit hours must be greater than zero.");
        }

        this.code = code.trim().toUpperCase();
        this.title = title.trim();
        this.creditHours = creditHours;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public abstract String getCategory();

    @Override
    public String toString() {
        return code + " - " + title + " (" + creditHours + " credits, " + getCategory() + ")";
    }
}
