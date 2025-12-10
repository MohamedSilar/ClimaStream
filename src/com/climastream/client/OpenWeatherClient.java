package com.climastream.client;

import com.climastream.dto.WeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

public class OpenWeatherClient {

    private final OkHttpClient http = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;
    private final String baseUrl;

    public OpenWeatherClient() throws Exception {

        Properties p = new Properties();

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            p.load(in);
        }

        this.apiKey = p.getProperty("owm.api.key");
        this.baseUrl = p.getProperty("owm.base.url");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing OpenWeatherMap API Key in application.properties");
        }
    }

    // Fetch JSON from API
    public JsonNode fetchWeatherJsonForCity(String city) throws Exception {

        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("q", city)
                .addQueryParameter("appid", apiKey)
                .build();

        Request req = new Request.Builder().url(url).get().build();

        try (Response resp = http.newCall(req).execute()) {

            if (!resp.isSuccessful()) {
                throw new RuntimeException("API Error: " + resp.code() + " - " + resp.message());
            }

            String body = resp.body().string();
            return mapper.readTree(body);
        }
    }

    // Convert JSON → WeatherDTO
    public WeatherDTO parseJsonToDto(JsonNode root) {

        WeatherDTO dto = new WeatherDTO();

        dto.setCity(root.path("name").asText());
        dto.setCountry(root.path("sys").path("country").asText());

        long timestamp = root.path("dt").asLong();
        dto.setTimestampUtc(Instant.ofEpochSecond(timestamp));

        double tempK = root.path("main").path("temp").asDouble();
        dto.setTempKelvin(tempK);
        dto.setTempCelsius(tempK - 273.15);

        dto.setHumidity(root.path("main").path("humidity").asInt());
        dto.setPressure(root.path("main").path("pressure").asInt());

        dto.setWindSpeed(root.path("wind").path("speed").asDouble());
        dto.setWindDeg(root.path("wind").path("deg").asInt());

        JsonNode weatherArray = root.path("weather");

        if (weatherArray.isArray() && weatherArray.size() > 0) {
            dto.setWeatherMain(weatherArray.get(0).path("main").asText());
            dto.setWeatherDescription(weatherArray.get(0).path("description").asText());
        }

        dto.setRawJson(root.toString());

        return dto;
    }
}
