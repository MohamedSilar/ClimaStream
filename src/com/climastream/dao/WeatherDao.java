package com.climastream.dao;

import com.climastream.dto.WeatherDTO;
import com.climastream.util.DbUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class WeatherDao {

    private final DataSource ds;

    public WeatherDao() {
        this.ds = DbUtil.getDataSource();
    }

    public void insert(WeatherDTO w) throws Exception {

        String sql =
                "INSERT INTO weather_readings (" +
                "city, country, timestamp_utc, temp_celsius, temp_kelvin, " +
                "humidity, pressure, wind_speed, wind_deg, weather_main, " +
                "weather_description, raw_json) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, w.getCity());
            ps.setString(2, w.getCountry());
            ps.setTimestamp(3, Timestamp.from(w.getTimestampUtc()));
            ps.setDouble(4, w.getTempCelsius());
            ps.setDouble(5, w.getTempKelvin());
            ps.setInt(6, w.getHumidity());
            ps.setInt(7, w.getPressure());
            ps.setDouble(8, w.getWindSpeed());
            ps.setInt(9, w.getWindDeg());
            ps.setString(10, w.getWeatherMain());
            ps.setString(11, w.getWeatherDescription());
            ps.setString(12, w.getRawJson());

            ps.executeUpdate();
        }
    }
}
