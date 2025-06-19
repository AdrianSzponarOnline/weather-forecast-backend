package com.example.weather.controller;

import com.example.weather.model.ForecastDay;
import com.example.weather.model.ForecastSummary;
import com.example.weather.service.ForecastService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class WeatherController {
    private final ForecastService forecastService;

    @Autowired
    public WeatherController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastDay>> forecast(
            @RequestParam("lat")
            @DecimalMin(value = "-90", message = "Szerokość geograficzna >= -90")
            @DecimalMax(value = "90", message = "Szerokość geograficzna <= 90")
            Double lat,
            @RequestParam("lon")
            @DecimalMin(value = "-180", message = "Długość geograficzna >= -180")
            @DecimalMax(value = "180", message = "Długość geograficzna <= 180")
            Double lon) {
        return ResponseEntity.ok(forecastService.getForecast(lat, lon));
    }

    @GetMapping("/forecast/summary")
    public ResponseEntity<ForecastSummary> summary(
            @RequestParam("lat")
            @DecimalMin(value = "-90", message = "Szerokość geograficzna >= -90")
            @DecimalMax(value = "90", message = "Szerokość geograficzna <= 90")
            Double lat,
            @RequestParam("lon")
            @DecimalMin(value = "-180", message = "Długość geograficzna >= -180")
            @DecimalMax(value = "180", message = "Długość geograficzna <= 180")
            Double lon) {
        return ResponseEntity.ok(forecastService.getSummary(lat, lon));
    }
}
