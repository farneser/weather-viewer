package com.farneser.weatherviewer.dto.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sys {
    @JsonProperty("country")
    private String country;
    @JsonProperty("sunrise")
    private long sunrise;
    @JsonProperty("sunset")
    private long sunset;
}