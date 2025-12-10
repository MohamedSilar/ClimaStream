package com.climastream;

import com.climastream.etl.WeatherETLWorker;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting ClimaStream Weather System...");

        // Load ETL interval from application.properties
        Properties p = new Properties();

        try (InputStream in = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            p.load(in);
        }

        int intervalSeconds = Integer.parseInt(
                p.getProperty("etl.poll.interval.seconds", "900")
        );

        // Create ETL worker
        WeatherETLWorker worker = WeatherETLWorker.fromConfig();

        // Schedule every X seconds
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
                worker,
                0,                 // run immediately
                intervalSeconds,   // repeat interval
                TimeUnit.SECONDS
        );

        System.out.println("ClimaStream ETL scheduled every " + intervalSeconds + " seconds.");

        // Prevent program from exiting
        Thread.currentThread().join();
    }
}
