package com.lawrence.transcript.model;

import com.lawrence.transcript.exception.InvalidInputException;

public class CoreCourse extends Course {
    public CoreCourse(String code, String title, int creditHours) throws InvalidInputException {
        super(code, title, creditHours);
    }

    @Override
    public String getCategory() {
        return "Core";
    }
}
