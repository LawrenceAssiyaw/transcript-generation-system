package com.lawrence.transcript.service;

import com.lawrence.transcript.exception.InvalidInputException;

public class AuthService {
    private static final String REGISTRAR_USERNAME = "registrar";
    private static final String REGISTRAR_PIN = "1234";

    public boolean login(String username, String pin) throws InvalidInputException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Username is required.");
        }
        if (pin == null || pin.trim().isEmpty()) {
            throw new InvalidInputException("PIN is required.");
        }
        if (!REGISTRAR_USERNAME.equalsIgnoreCase(username.trim()) || !REGISTRAR_PIN.equals(pin.trim())) {
            throw new InvalidInputException("Wrong registrar username or PIN.");
        }
        return true;
    }
}
