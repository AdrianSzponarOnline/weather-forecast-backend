package com.example.weather.service;
import com.example.weather.client.WeatherApiClient;
import com.example.weather.dto.OpenMeteoResponse;
import com.example.weather.model.ForecastDay;
import com.example.weather.model.ForecastSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ForecastService {
    private static final double PANEL_POWER_KW = 2.5;
    private static final double PANEL_EFFICENCY = 0.20;

    private static final Set<Integer> RAIN_CODES = Set.of(
            51, 53, 55,   // mżawka
            56, 57,       // marznąca mżawka
            61, 63, 65,   // deszcz
            66, 67,       // marznący deszcz
            80, 81, 82,   // zlewa
            95, 96, 99    // burze
    );


    private final WeatherApiClient client;
    @Autowired
    public ForecastService(WeatherApiClient client) {
        this.client = client;
    }

    public List<ForecastDay> getForecast(double lat, double lon) {
        OpenMeteoResponse api = client.fetch(lat, lon);

        List<ForecastDay> days = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            double sunshineHours = api.daily().sunshineDuration().get(i) / 3600.0;
            double energyKwh = PANEL_POWER_KW * sunshineHours * PANEL_EFFICENCY;

            days.add(new ForecastDay(
                    api.daily().time().get(i),
                    api.daily().weatherCode().get(i),
                    api.daily().temperature2mMin().get(i),
                    api.daily().temperature2mMax().get(i),
                    energyKwh
            ));
        }
        return days;
    }

    public ForecastSummary getSummary(double lat, double lon) {
        OpenMeteoResponse api = client.fetch(lat, lon);

        double avgPressure = api.daily().surfacePressureMean().stream()
                .mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

        double avgSunshine = api.daily().sunshineDuration().stream()
                .mapToInt(Integer::intValue).average().orElse(0) / 3600.0;

        double weekMin = api.daily().temperature2mMin().stream()
                .mapToDouble(Double::doubleValue).min().orElse(Double.NaN);

        double weekMax = api.daily().temperature2mMax().stream()
                .mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

        long rainyDays = api.daily().weatherCode().stream()
                .filter(RAIN_CODES::contains).count();
        String summary = rainyDays >= 4 ? "z opadami" : "bez opadów";

        return new ForecastSummary(weekMin, weekMax, avgPressure, avgSunshine, summary);
    }



}
