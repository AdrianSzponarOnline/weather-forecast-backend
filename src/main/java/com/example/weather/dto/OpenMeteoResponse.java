package com.example.weather.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenMeteoResponse(Daily daily) {
    public record Daily(
            List<String> time,

            @JsonProperty("weathercode")
            List<Integer> weatherCode,

            @JsonProperty("temperature_2m_max")
            List<Double> temperature2mMax,

            @JsonProperty("temperature_2m_min")
            List<Double> temperature2mMin,

            @JsonProperty("sunshine_duration")
            List<Integer> sunshineDuration,// sekundy

            @JsonProperty("surface_pressure_mean")
            List<Double> surfacePressureMean){} //hPa

}

