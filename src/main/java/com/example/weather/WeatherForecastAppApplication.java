package com.example.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherForecastAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastAppApplication.class, args);
	}

}
