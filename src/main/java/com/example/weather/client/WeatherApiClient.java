package com.example.weather.client;
import com.example.weather.dto.OpenMeteoResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class WeatherApiClient {

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String URL =
            "https://api.open-meteo.com/v1/forecast" +
            "?latitude={lat}&longitude={lon}" +
            "&daily=weathercode,temperature_2m_max,temperature_2m_min," +
            "sunshine_duration,surface_pressure_mean" +
            "&hourly=surface_pressure" +
            "&forecast_days=7&timezone=auto";

    @Cacheable(cacheNames = "weatherCache", key = "#lat + ':' + #lon")
    public OpenMeteoResponse fetch(double lat, double lon) {
        Map<String, Object> params = Map.of("lat", lat, "lon", lon);
        return restTemplate.getForObject(URL, OpenMeteoResponse.class, params);
    }
}
