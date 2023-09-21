package com.farneser.weatherviewer.exceptions;

public class ParamNotExistsException extends Exception {
    public ParamNotExistsException(String paramName) {
        super("Param: " + paramName + " not found");
    }
}
