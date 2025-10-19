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
    private final String baseUrl;
    private final String dailyParams;
    private final int forecastDays;
    private final String timezone;

    @Autowired
    public WeatherApiClient(RestTemplate restTemplate,
                           @Value("${weather.api.base-url}") String baseUrl,
                           @Value("${weather.api.daily-params}") String dailyParams,
                           @Value("${weather.api.forecast-days}") int forecastDays,
                           @Value("${weather.api.timezone}") String timezone) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.dailyParams = dailyParams;
        this.forecastDays = forecastDays;
        this.timezone = timezone;
    }

    @Cacheable(cacheNames = "weatherCache", key = "#lat + ':' + #lon")
    public OpenMeteoResponse fetch(double lat, double lon) {
        System.out.printf("CALL Open-Meteo for %f,%f", lat, lon);
        
        String url = String.format("%s?latitude={lat}&longitude={lon}&daily=%s&forecast_days=%d&timezone=%s",
                baseUrl, dailyParams, forecastDays, timezone);
        
        Map<String, Object> params = Map.of("lat", lat, "lon", lon);
        return restTemplate.getForObject(url, OpenMeteoResponse.class, params);
    }
}
