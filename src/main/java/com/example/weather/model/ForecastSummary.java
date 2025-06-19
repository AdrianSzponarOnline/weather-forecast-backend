package com.example.weather.model;

public record ForecastSummary(
        double minTemperature,
        double maxTemperature,
        double avgPressure,
        double avgSunshineHours,
        String weekComment) {}
