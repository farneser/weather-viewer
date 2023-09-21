package com.farneser.weatherviewer.dto.api.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("id")
    private int id;
    @JsonProperty("main")
    private String main;

    public String getDescription() {
        if (description == null || description.isEmpty()) {
            return description;
        }

        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;

    public String getIconUrl() {
        return "https://openweathermap.org/img/wn/" + icon + "@4x.png";
    }
}