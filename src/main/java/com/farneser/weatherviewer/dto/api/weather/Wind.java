package com.farneser.weatherviewer.dto.api.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {
    @JsonProperty("speed")
    private double speed;
    @JsonProperty("deg")
    private int deg;
    @JsonProperty("gust")
    private double gust;
}