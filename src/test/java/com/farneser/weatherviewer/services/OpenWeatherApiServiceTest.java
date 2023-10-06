package com.farneser.weatherviewer.services;

import com.farneser.weatherviewer.dto.api.LocationResponse;
import com.farneser.weatherviewer.dto.api.WeatherResponse;
import com.farneser.weatherviewer.dto.api.weather.*;
import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.ApiUriFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
            "    \"country\": \"UK\",\n" +
            "    \"state\": \"England\"\n" +
            "  }\n" +
            "]";
    @Mock
    private HttpClient httpClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private OpenWeatherApiService apiService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        try {
            ApiUriFactory.build(ApiUriFactory.getApiKey());
        } catch (InternalServerException e) {
            throw new RuntimeException(e);
        }

        apiService = new OpenWeatherApiService(httpClient, objectMapper);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetLocationsByName() throws IOException, InterruptedException {
        // Arrange
        var locationName = "London";
        var expectedResponse = List.of(
                new LocationResponse("London", 51.5073219, -0.1276474, "UK")
        );

        var response = Mockito.mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(locationResponseBody);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

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

        expectedResponse.setMain(new Main(15.86, 15.62, 14.38, 17.07, 1020, 81, 0, 0));

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

        WeatherResponse actualResponse = null;
        try {
            actualResponse = apiService.getWeatherByLocation(lat, lon);
        } catch (InternalServerException e) {
            // FIXME: 10/3/23 
        }

        assertEquals(expectedResponse, actualResponse);
    }
}
