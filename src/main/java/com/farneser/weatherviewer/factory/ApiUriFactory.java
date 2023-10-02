package com.farneser.weatherviewer.factory;

import com.farneser.weatherviewer.exceptions.InternalServerException;

import java.net.URI;

public abstract class ApiUriFactory {
    private static final String baseUrl = "https://api.openweathermap.org";
    private static final String weatherApiPath = "/data/2.5/weather";
    private static final String directApiPath = "/geo/1.0/direct";
    private static String apiKey;

    public static void build(String apiKey) {
        ApiUriFactory.apiKey = apiKey;
    }

    public static URI buildDirectUri(String locationName) throws InternalServerException {
        return URI.create(baseUrl + directApiPath + "?q=" + locationName.replace(" ", "+") + "&limit=10" + getApiKeyPath());
    }

    public static URI buildWeatherUri(double latitude, double longitude) throws InternalServerException {
        return URI.create(baseUrl + weatherApiPath + "?lat=" + latitude + "&lon=" + longitude + "&units=metric" + getApiKeyPath());
    }

    private static String getApiKeyPath() throws InternalServerException {
        if (apiKey == null) {
            throw new InternalServerException("missing open weather api key");
        }
        return "&appid=" + apiKey;
    }
}
