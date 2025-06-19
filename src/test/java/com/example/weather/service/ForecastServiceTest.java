package com.example.weather.service;

import com.example.weather.client.WeatherApiClient;
import com.example.weather.dto.OpenMeteoResponse;
import com.example.weather.model.ForecastDay;
import com.example.weather.model.ForecastSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForecastServiceTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @InjectMocks
    private ForecastService forecastService;


    @Test
    @DisplayName("getForecast zwraca 7 elementów")
    void shouldReturnSevenForecastDays() {
        when(weatherApiClient.fetch(50.0, 20.0))
                .thenReturn(mockResponse(61, 7, 72000));

        List<ForecastDay> result = forecastService.getForecast(50.0, 20.0);
        assertThat(result)
                .hasSize(7)
                .allSatisfy(day ->
                        assertThat(day.energyKwh())
                                .isGreaterThan(0));
    }
    @Test
    @DisplayName("getForecast poprawnie liczy energyKwh")
    void shouldCalculateCorrectEnergy() {
        // 20 h słońca → 2.5 * 20 * 0.2 = 10 kWh
        when(weatherApiClient.fetch(0.0, 0.0))
                .thenReturn(mockResponse(61, 7, 72_000));

        ForecastDay first = forecastService.getForecast(0.0, 0.0).get(0);
        assertThat(first.energyKwh()).isCloseTo(10.0, within(0.001));
    }
    @Test
    @DisplayName("getSummary liczy średnie ciśnienie i słońce")
    void shouldComputeAverages() {
        // ciśnienia 1000-1006, słońce 10 h (= 36 000 s)
        when(weatherApiClient.fetch(1.0, 1.0))
                .thenReturn(mockResponse(61, 7, 36_000));

        ForecastSummary summary = forecastService.getSummary(1.0, 1.0);

        assertThat(summary.avgPressure()).isCloseTo(1003.0, within(0.001));
        assertThat(summary.avgSunshineHours()).isCloseTo(10.0, within(0.001));
    }
    @Test
    @DisplayName("getSummary zwraca 'z opadami', gdy >=4 dni deszczowe")
    void shouldReturnRainySummary() {
        // 4 razy kod 61 (deszcz), 3 razy 1 (bez opadu)
        when(weatherApiClient.fetch(2.0, 2.0))
                .thenReturn(mockResponse(List.of(61,61,61,61,1,1,1), 36_000));

        ForecastSummary summary = forecastService.getSummary(2.0, 2.0);

        assertThat(summary.weekComment()).isEqualTo("z opadami");
    }

    @Test
    @DisplayName("getSummary zwraca 'bez opadów', gdy <4 dni deszczowych")
    void shouldReturnDrySummary() {
        // 2 razy kod 61, reszta 1
        when(weatherApiClient.fetch(3.0, 3.0))
                .thenReturn(mockResponse(List.of(61,61,1,1,1,1,1), 36_000));

        ForecastSummary summary = forecastService.getSummary(3.0, 3.0);

        assertThat(summary.weekComment()).isEqualTo("bez opadów");
    }

    private OpenMeteoResponse mockResponse(int weatherCode, int days, int sunshineSec) {
        List<Integer> codes = IntStream.range(0, days).map(i -> weatherCode).boxed().toList();
        return mockResponse(codes, sunshineSec);
    }
    private OpenMeteoResponse mockResponse(List<Integer> weatherCodes, int sunshineSecEach) {

        List<String> dates = IntStream.range(0, 7)
                .mapToObj(i -> "2025-06-1" + (i + 9))   // 19-25
                .toList();

        List<Double> tMin = List.of(10d,11d,12d,13d,14d,15d,16d);
        List<Double> tMax = List.of(20d,21d,22d,23d,24d,25d,26d);

        List<Integer> sunshine = IntStream.range(0, 7)
                .map(i -> sunshineSecEach).boxed().toList();

        List<Double> pressure = IntStream.range(0, 7)
                .mapToDouble(i -> 1000 + i).boxed().toList();

        OpenMeteoResponse.Daily daily = new OpenMeteoResponse.Daily(
                dates,
                weatherCodes,
                tMin,
                tMax,
                sunshine,
                pressure
        );

        return new OpenMeteoResponse(daily);
    }
}