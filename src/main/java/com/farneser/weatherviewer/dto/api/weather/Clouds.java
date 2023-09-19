package com.farneser.weatherviewer.dto.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Clouds {
    @JsonProperty("all")
    private int all;
}