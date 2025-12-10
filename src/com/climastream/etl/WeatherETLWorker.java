package com.climastream.etl;

import com.climastream.client.OpenWeatherClient;
import com.climastream.dao.WeatherDao;
import com.climastream.dto.WeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherETLWorker implements Runnable {

    private final OpenWeatherClient client;
    private final WeatherDao dao;
    private final List<String> cities;

    public WeatherETLWorker(List<String> cities) throws Exception {
        this.client = new OpenWeatherClient();
        this.dao = new WeatherDao();
        this.cities = cities;
    }

    @Override
    public void run() {

        ExecutorService pool = Executors.newFixedThreadPool(Math.min(cities.size(), 5));

        for (String city : cities) {

            pool.submit(() -> {
                try {
                    // Step 1: fetch JSON
                    JsonNode json = client.fetchWeatherJsonForCity(city);

                    // Step 2: convert JSON -> DTO
                    WeatherDTO dto = client.parseJsonToDto(json);

                    // Step 3: insert into DB
                    dao.insert(dto);

                    System.out.println("Inserted weather data for: " + dto.getCity());
                }
                catch (Exception e) {
                    System.err.println("Error fetching data for " + city + ": " + e.getMessage());
                }
            });
        }

        pool.shutdown();
    }

    // Loads cities from application.properties
    public static WeatherETLWorker fromConfig() throws Exception {

        Properties p = new Properties();

        try (InputStream in = WeatherETLWorker.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            p.load(in);
        }

        String cityString = p.getProperty("etl.cities", "London");
        List<String> cities = Arrays.stream(cityString.split(","))
                                    .map(String::trim)
                                    .toList();

        return new WeatherETLWorker(cities);
    }
}
