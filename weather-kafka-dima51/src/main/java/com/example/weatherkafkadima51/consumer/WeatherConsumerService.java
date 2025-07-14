package com.example.weatherkafkadima51.consumer;

import com.example.weatherkafkadima51.model.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class WeatherConsumerService {

    private final ObjectMapper objectMapper;
    private final Map<String, List<WeatherData>> weatherByCity = new HashMap<>();

    private WeatherData hottestDay = null;

    public WeatherConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "weather", groupId = "weather-group")
    public void consume(String message) {
        try {
            WeatherData data = objectMapper.readValue(message, WeatherData.class);
            System.out.println("Received: " + data);

            weatherByCity.computeIfAbsent(data.getCity(), c -> new ArrayList<>()).add(data);

            if (hottestDay == null || data.getTemperature() > hottestDay.getTemperature()) {
                hottestDay = data;
            }

            printAnalytics();

        } catch (JsonProcessingException e) {
            System.err.println("Ошибка при парсинге JSON: " + e.getMessage());
        }
    }

    private void printAnalytics() {
        System.out.println("\nАналитика на текущий момент:");

        WeekFields weekFields = WeekFields.ISO;
        int currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());

        List<String> cities = new ArrayList<>(weatherByCity.keySet());

        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            List<WeatherData> records = weatherByCity.get(city);

            List<WeatherData> thisWeek = records.stream().filter(d -> d.getDate().get(weekFields.weekOfWeekBasedYear()) == currentWeek).toList();

            long rainyDays = thisWeek.stream().filter(d -> d.getCondition().toLowerCase().contains("дожд")).map(WeatherData::getDate).distinct().count();

            OptionalDouble avgTemp = thisWeek.stream().mapToInt(WeatherData::getTemperature).average();

            System.out.printf("Город: %s | Дождливых дней: %d | Средняя температура: %.1f°C%n", city, rainyDays, avgTemp.orElse(0.0));
        }


        if (hottestDay != null) {
            System.out.printf("Самый жаркий день: %s в городе %s (%d°C)%n", hottestDay.getDate(), hottestDay.getCity(), hottestDay.getTemperature());
        }

        System.out.println();
    }
}
