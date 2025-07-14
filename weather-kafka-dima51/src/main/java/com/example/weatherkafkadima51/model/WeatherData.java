package com.example.weatherkafkadima51.model;

import java.time.LocalDate;

public class WeatherData {
    private String city;
    private int temperature;
    private String condition;
    private LocalDate date;

    public WeatherData() {
    }

    public WeatherData(String city, int temperature, String condition, LocalDate date) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //Данные
    @Override
    public String toString() {
        return "WeatherData{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                ", condition='" + condition + '\'' +
                ", date=" + date +
                '}';
    }
}