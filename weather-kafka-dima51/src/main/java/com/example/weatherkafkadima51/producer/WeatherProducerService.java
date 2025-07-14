package com.example.weatherkafkadima51.producer;

import com.example.weatherkafkadima51.model.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class WeatherProducerService {

    private static final List<String> CITIES = List.of("Москва", "Санкт-Петербург", "Чукотка", "Екатеринбург", "Тюмень");
    private static final List<String> CONDITIONS = List.of("солнечно", "облачно", "дождь");
    private final Random random = new Random();

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public WeatherProducerService(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 2000)
    public void sendWeather() {
        String city = CITIES.get(random.nextInt(CITIES.size()));
        int temperature = random.nextInt(36);
        String condition = CONDITIONS.get(random.nextInt(CONDITIONS.size()));
        LocalDate date = LocalDate.now();

        WeatherData weatherData = new WeatherData(city, temperature, condition, date);

        try {
            String message = objectMapper.writeValueAsString(weatherData);
            kafkaTemplate.send("weather", city, message);
            System.out.println("Sent: " + message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
