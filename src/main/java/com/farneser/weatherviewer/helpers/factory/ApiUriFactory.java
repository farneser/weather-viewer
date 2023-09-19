package com.farneser.weatherviewer.helpers.factory;

import java.net.URI;

public class ApiUriFactory {
    private static String apiKey;
    private final String baseUrl;
    private static final String weatherApiPath = "/data/2.5/weather";
    private static final String directApiPath = "/geo/1.0/direct";

    public ApiUriFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static void build(String apiKey) {
        ApiUriFactory.apiKey = apiKey;
    }

    public URI buildDirect(String locationName) {
        return URI.create(baseUrl + directApiPath + "?q=" + locationName + "&limit=10" + getApiKeyPath());
    }

    public URI buildWeather(double latitude, double longitude){
        return URI.create(baseUrl + weatherApiPath + "?lat=" + latitude + "&lon=" + longitude + getApiKeyPath());
    }

    private String getApiKeyPath() {
        return "&appid=" + apiKey;
    }
}
