package com.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            Map<String, String> properties = new HashMap<>();

            String dbName = dotenv.get("DB_NAME");
            String masterUrl = dotenv.get("DB_MASTER_URL");
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String pass = dotenv.get("DB_PASSWORD");
            String driver = dotenv.get("DB_DRIVER");

            if (dbName != null && url != null) {
                try {
                    try (Connection conn = DriverManager.getConnection(masterUrl, user, pass);
                        Statement stmt = conn.createStatement()) {
                        String sql = "IF DB_ID('" + dbName.replace("'", "''") + "') IS NULL CREATE DATABASE [" + dbName + "]";
                        stmt.executeUpdate(sql);
                    }
                } catch (Exception e) {
                    System.err.println("Could not create database: " + e.getMessage());
                }
            }

            properties.put("jakarta.persistence.jdbc.driver", driver);
            properties.put("jakarta.persistence.jdbc.url", url);
            properties.put("jakarta.persistence.jdbc.user", user);
            properties.put("jakarta.persistence.jdbc.password", pass);

            entityManagerFactory = Persistence.createEntityManagerFactory("MyPU", properties);

        } catch (Throwable ex) {
            System.err.println("Can't initiate EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static void shutdown() {
        getEntityManagerFactory().close();
    }
}
