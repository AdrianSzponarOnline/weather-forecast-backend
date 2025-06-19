package com.example.weather.dto;

import java.util.List;

public record OpenMeteoResponse(Daily daily, Hourly hourly) {
    public record Daily(
        List<String> time,
        List<Integer> weatherCode,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min,
        List<Integer> sunshine_duration, // sekundy
        List<Double> surface_pressure_mean){} //hPa

    public record Hourly(
            List<String> time,
            List<Double> surface_pressure){}
}

