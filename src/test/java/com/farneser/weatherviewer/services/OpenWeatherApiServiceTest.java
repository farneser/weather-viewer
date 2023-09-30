package com.farneser.weatherviewer.services;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.dto.api.weather.*;
import com.farneser.weatherviewer.helpers.factory.ApiUriFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OpenWeatherApiServiceTest {
    private static final String weatherResponseBody = "{\n" +
            "  \"coord\": {\n" +
            "    \"lon\": -0.1276,\n" +
            "    \"lat\": 51.5073\n" +
            "  },\n" +
            "  \"weather\": [\n" +
            "    {\n" +
            "      \"id\": 802,\n" +
            "      \"main\": \"Clouds\",\n" +
            "      \"description\": \"scattered clouds\",\n" +
            "      \"icon\": \"03n\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"base\": \"stations\",\n" +
            "  \"main\": {\n" +
            "    \"temp\": 15.86,\n" +
            "    \"feels_like\": 15.62,\n" +
            "    \"temp_min\": 14.38,\n" +
            "    \"temp_max\": 17.07,\n" +
            "    \"pressure\": 1020,\n" +
            "    \"humidity\": 81\n" +
            "  },\n" +
            "  \"visibility\": 10000,\n" +
            "  \"wind\": {\n" +
            "    \"speed\": 4.02,\n" +
            "    \"deg\": 147,\n" +
            "    \"gust\": 4.92\n" +
            "  },\n" +
            "  \"clouds\": {\n" +
            "    \"all\": 35\n" +
            "  },\n" +
            "  \"dt\": 1696108515,\n" +
            "  \"sys\": {\n" +
            "    \"type\": 2,\n" +
            "    \"id\": 2075535,\n" +
            "    \"country\": \"GB\",\n" +
            "    \"sunrise\": 1696053522,\n" +
            "    \"sunset\": 1696095747\n" +
            "  },\n" +
            "  \"timezone\": 3600,\n" +
            "  \"id\": 2643743,\n" +
            "  \"name\": \"London\",\n" +
            "  \"cod\": 200\n" +
            "}";
    private static final String locationResponseBody = "[\n" +
            "  {\n" +
            "    \"name\": \"London\",\n" +
            "    \"lat\": 51.5073219,\n" +
            "    \"lon\": -0.1276474,\n" +
            "    \"country\": \"GB\",\n" +
            "    \"state\": \"England\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"City of London\",\n" +
            "    \"lat\": 51.5156177,\n" +
            "    \"lon\": -0.0919983,\n" +
            "    \"country\": \"GB\",\n" +
            "    \"state\": \"England\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"London\",\n" +
            "    \"lat\": 42.9832406,\n" +
            "    \"lon\": -81.243372,\n" +
            "    \"country\": \"CA\",\n" +
            "    \"state\": \"Ontario\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Chelsea\",\n" +
            "    \"lat\": 51.4875167,\n" +
            "    \"lon\": -0.1687007,\n" +
            "    \"country\": \"GB\",\n" +
            "    \"state\": \"England\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"London\",\n" +
            "    \"lat\": 37.1289771,\n" +
            "    \"lon\": -84.0832646,\n" +
            "    \"country\": \"US\",\n" +
            "    \"state\": \"Kentucky\"\n" +
            "  }\n" +
            "]";
    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    private OpenWeatherApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ApiUriFactory.build(System.getenv("OPENWEATHERMAP_APIKEY_ENV_VARIABLE"));
        apiService = new OpenWeatherApiService(httpClient, objectMapper);
    }

    @Test
    void testGetLocationsByName() throws IOException, InterruptedException {
        // Arrange
        var locationName = "London";
        var expectedResponse = List.of(
                new LocationResponse("London", 51.5074, -0.1278, "UK")
        );

        var response = Mockito.mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(locationResponseBody);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        when(objectMapper.readValue(any(String.class), any(TypeReference.class)))
                .thenReturn(expectedResponse);

        var actualResponse = apiService.getLocationsByName(locationName);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetWeatherByLocation() throws IOException, InterruptedException {
        var lat = 51.5074;
        var lon = -0.1278;

        var expectedResponse = new WeatherResponse();
        expectedResponse.setCoordinates(new Coordinates(-0.1276, 51.5073));

        var weather = new Weather[1];

        weather[0] = new Weather(802, "Clouds", "scattered clouds", "03n");

        expectedResponse.setWeather(weather);

        expectedResponse.setBase("stations");

        expectedResponse.setMain(new Main(15.86, 15.62, 14.38, 17.07, 1020, 81, 10000, -1));

        expectedResponse.setVisibility(10000);

        expectedResponse.setWind(new Wind(4.02, 147, 4.92));

        expectedResponse.setClouds(new Clouds(35));

        expectedResponse.setDt(1696108515);
        expectedResponse.setTimezone(3600);
        expectedResponse.setName("London");

        expectedResponse.setSys(new Sys("GB", 1696053522, 1696095747));


        var response = Mockito.mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(weatherResponseBody);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        when(objectMapper.readValue(any(String.class), any(Class.class)))
                .thenReturn(expectedResponse);

        var actualResponse = apiService.getWeatherByLocation(lat, lon);

        assertEquals(expectedResponse, actualResponse);
    }
}
