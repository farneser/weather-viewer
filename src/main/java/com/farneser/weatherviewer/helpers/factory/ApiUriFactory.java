package com.farneser.weatherviewer.helpers.factory;

import java.net.URI;

public abstract class ApiUriFactory {
    private static final String baseUrl = "https://api.openweathermap.org";
    private static final String weatherApiPath = "/data/2.5/weather";
    private static final String directApiPath = "/geo/1.0/direct";
    private static String apiKey;

    public static void build(String apiKey) {
        ApiUriFactory.apiKey = apiKey;
    }

    public static URI buildDirect(String locationName) {
        return URI.create(baseUrl + directApiPath + "?q=" + locationName + "&limit=10" + getApiKeyPath());
    }

    public static URI buildWeather(double latitude, double longitude) {
        return URI.create(baseUrl + weatherApiPath + "?lat=" + latitude + "&lon=" + longitude + getApiKeyPath());
    }

    private static String getApiKeyPath() {
        return "&appid=" + apiKey;
    }
}
