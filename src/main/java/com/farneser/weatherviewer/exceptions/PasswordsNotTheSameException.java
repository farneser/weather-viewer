package com.farneser.weatherviewer.exceptions;

public class PasswordsNotTheSameException extends Exception {
    public PasswordsNotTheSameException() {
        super("Passwords must match");
    }
}
