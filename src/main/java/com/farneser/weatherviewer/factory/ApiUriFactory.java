package com.farneser.weatherviewer.factory;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.listeners.ApplicationContextListener;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

public abstract class ApiUriFactory {
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static final String WEATHER_API_PATH = "/data/2.5/weather";
    private static final String DIRECT_API_PATH = "/geo/1.0/direct";
    private static String apiKey;

    public static void build(String apiKey) {
        ApiUriFactory.apiKey = apiKey;
    }

    public static URI buildDirectUri(String locationName) throws InternalServerException {
        return URI.create(BASE_URL + DIRECT_API_PATH + "?q=" + locationName.replace(" ", "+") + "&limit=10" + getApiKeyPath());
    }

    public static URI buildWeatherUri(double latitude, double longitude) throws InternalServerException {
        return URI.create(BASE_URL + WEATHER_API_PATH + "?lat=" + latitude + "&lon=" + longitude + "&units=metric" + getApiKeyPath());
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
