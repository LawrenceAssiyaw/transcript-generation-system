package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

public class ElectiveCourse extends Course {
    public ElectiveCourse(String code, String title, int creditHours) throws InvalidInputException {
        super(code, title, creditHours);
    }

    @Override
    public String getCategory() {
        return "Elective";
    }
}
