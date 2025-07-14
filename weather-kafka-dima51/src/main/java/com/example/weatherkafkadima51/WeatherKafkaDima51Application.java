package com.example.weatherkafkadima51;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherKafkaDima51Application {

	public static void main(String[] args) {
		SpringApplication.run(WeatherKafkaDima51Application.class, args);
	}
}
