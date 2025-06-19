package com.example.weather.model;

public record ForecastDay(
        String date,
        int weatherCode,
        double tempMin,
        double tempMax,
        double energyKwh
) {

}
