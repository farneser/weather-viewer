package com.farneser.weatherviewer.services;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.helpers.factory.ApiUriFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class OpenWeatherApiService {
    private static final ApiUriFactory apiFactory = new ApiUriFactory("https://api.openweathermap.org");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<LocationResponse> getLocationsByName(String locationName) {

        try {
            var url = apiFactory.buildDirect(locationName);

            var request = HttpRequest.newBuilder(url)
                    .GET()
                    .build();

            var client = HttpClient.newHttpClient();


            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));

            return null;
        }
    }
}
