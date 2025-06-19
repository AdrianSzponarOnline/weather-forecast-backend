package com.example.weather.controller;

import com.example.weather.model.ForecastDay;
import com.example.weather.model.ForecastSummary;
import com.example.weather.service.ForecastService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForecastService forecastService;

    @Test
    @DisplayName("GET /api/forecast - poprawne parametry")
    void shouldReturnForecast() throws Exception {
        Mockito.when(forecastService.getForecast(anyDouble(), anyDouble()))
                .thenReturn(List.of(new ForecastDay("2025-06-19", 1, 10, 20, 5)));

        mockMvc.perform(get("/api/forecast")
                .param("lat", "50.0")
                .param("lon", "20.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/forecast - brak parametru lat")
    void shouldReturnBadRequestWhenLatMissing() throws Exception {
        mockMvc.perform(get("/api/forecast")
                .param("lon", "20.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/forecast - nieprawidłowy zakres lat")
    void shouldReturnBadRequestWhenLatOutOfRange() throws Exception {
        mockMvc.perform(get("/api/forecast")
                .param("lat", "100.0")
                .param("lon", "20.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Niepoprawne parametry wejściowe"));
    }

    @Test
    @DisplayName("GET /api/forecast/summary - poprawne parametry")
    void shouldReturnSummary() throws Exception {
        Mockito.when(forecastService.getSummary(anyDouble(), anyDouble()))
                .thenReturn(new ForecastSummary(10, 20, 1010, 8, "bez opadów"));

        mockMvc.perform(get("/api/forecast/summary")
                .param("lat", "50.0")
                .param("lon", "20.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/forecast/summary - nieprawidłowy zakres lon")
    void shouldReturnBadRequestWhenLonOutOfRange() throws Exception {
        mockMvc.perform(get("/api/forecast/summary")
                .param("lat", "50.0")
                .param("lon", "200.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Niepoprawne parametry wejściowe"));
    }
} 