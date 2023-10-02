package com.farneser.weatherviewer.utils;


import jakarta.servlet.http.HttpServletRequest;

public class ParameterParser {
    public static String getStringParam(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static Double getDoubleParam(HttpServletRequest request, String paramName) {
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
