package com.farneser.weatherviewer.factory;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.listeners.ApplicationContextListener;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

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

    public static String getApiKey() throws InternalServerException {
        var apiKey = System.getenv("OPENWEATHER_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            var properties = new Properties();

            var resourceUrl = ApplicationContextListener.class.getClassLoader().getResource("config.properties");

            if (resourceUrl != null) {
                try {
                    properties.load(new FileInputStream(resourceUrl.toURI().getPath()));

                    apiKey = properties.getProperty("weather_api_key");
                } catch (Exception e) {
                    throw new InternalServerException("invalid api key");
                }
            }
        }

        return apiKey;
    }
}
