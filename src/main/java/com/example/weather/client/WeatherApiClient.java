package com.example.weather.client;
import com.example.weather.dto.OpenMeteoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Component
public class WeatherApiClient {

    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public WeatherApiClient(RestTemplate restTemplate, @Value("${weather.api.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Cacheable(cacheNames = "weatherCache", key = "#lat + ':' + #lon")
    public OpenMeteoResponse fetch(double lat, double lon) {
        System.out.printf("CALL Open-Meteo for %f,%f", lat, lon);
        Map<String, Object> params = Map.of("lat", lat, "lon", lon);
        return restTemplate.getForObject(url, OpenMeteoResponse.class, params);
    }
}
