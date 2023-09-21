package com.farneser.weatherviewer.helpers.parser;


import jakarta.servlet.http.HttpServletRequest;

public class ParameterParser {
    public static String getString(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static Double getDouble(HttpServletRequest request, String paramName) {
        var paramValue = request.getParameter(paramName);

        if (paramValue != null && !paramValue.isEmpty()) {
            try {
                return Double.parseDouble(paramValue);
            } catch (NumberFormatException e) {
                return (double) 0;
            }
        }

        return (double) 0;
    }
}
