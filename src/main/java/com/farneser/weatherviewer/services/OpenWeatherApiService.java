package com.farneser.weatherviewer.services;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.helpers.factory.ApiUriFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenWeatherApiService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .GET()
                .build();
    }

    public List<LocationResponse> getLocationsByName(String locationName) {

        try {
            var request = buildRequest(ApiUriFactory.buildDirect(locationName));

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));

            return new ArrayList<>();
        }
    }


    public WeatherResponse getWeatherByLocation(double lat, double lon) {
        try {

            var request = buildRequest(ApiUriFactory.buildWeather(lat, lon));

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), WeatherResponse.class);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));

            return null;
        }
    }
}
