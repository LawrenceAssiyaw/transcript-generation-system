package com.lawrence.transcript.service;

public class ClassificationService {
    public String classify(double cgpa) {
        if (cgpa >= 3.6) {
            return "First Class";
        }
        if (cgpa >= 3.0) {
            return "Second Class Upper";
        }
        if (cgpa >= 2.5) {
            return "Second Class Lower";
        }
        if (cgpa >= 2.0) {
            return "Third Class";
        }
        return "Pass / Fail";
    }
}
