package com.farneser.weatherviewer.services;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.ApiUriFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class OpenWeatherApiService {
    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public OpenWeatherApiService() {
    }

    public OpenWeatherApiService(HttpClient httpClient, ObjectMapper objectMapper) {
        client = httpClient;
        this.objectMapper = objectMapper;
    }

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .GET()
                .build();
    }

    public List<LocationResponse> getLocationsByName(String locationName) {
        try {
            var request = buildRequest(ApiUriFactory.buildDirectUri(locationName));

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {
                });
            } else {
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException | InternalServerException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return new ArrayList<>();
        }
    }

    public WeatherResponse getWeatherByLocation(double lat, double lon) throws InternalServerException {
        try {
            var request = buildRequest(ApiUriFactory.buildWeatherUri(lat, lon));

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), WeatherResponse.class);
            } else {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            throw new InternalServerException("api service internal exception");
        }
    }
}
