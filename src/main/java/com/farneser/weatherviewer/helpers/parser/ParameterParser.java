package com.farneser.weatherviewer.helpers.parser;


import jakarta.servlet.http.HttpServletRequest;

public class ParameterParser {
    public static String getStringParameter(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static int getIntParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        if (paramValue != null && !paramValue.isEmpty()) {
            try {
                return Integer.parseInt(paramValue);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }
}
