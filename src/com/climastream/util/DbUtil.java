package com.climastream.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class DbUtil {

    private static HikariDataSource ds;

    static {
        try (InputStream in = DbUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties p = new Properties();
            p.load(in);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(p.getProperty("db.url"));
            config.setUsername(p.getProperty("db.user"));
            config.setPassword(p.getProperty("db.password"));
            config.setMaximumPoolSize(10);
            config.setPoolName("ClimaStreamPool");

            ds = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection: " + e.getMessage(), e);
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static void closePool() {
        if (ds != null) {
            ds.close();
        }
    }
}
