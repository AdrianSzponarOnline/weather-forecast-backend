package com.example.weather.model;

import lombok.Getter;

@Getter
public record ForecastDay(
        String date,
        int weatherCode,
        double tempMin,
        double tempMax,
        double energyKwh
) {

}
