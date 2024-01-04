package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;

public class WeatherDatamart implements WeatherStorer {

    @Override
    public void storeWeather(String event, String topicName) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            JsonObject location = jsonObject.getAsJsonObject("location");
            String name = location.getAsJsonPrimitive("name").getAsString();
            String clouds = jsonObject.getAsJsonPrimitive("clouds").getAsString();
            String windSpeed = jsonObject.getAsJsonPrimitive("windSpeed").getAsString();
            String temperature = jsonObject.getAsJsonPrimitive("temperature").getAsString();
            String humidity = jsonObject.getAsJsonPrimitive("humidity").getAsString();
            String instant = jsonObject.getAsJsonPrimitive("instant").getAsString();
            String precipitation = jsonObject.getAsJsonPrimitive("precipitation").getAsString();
            String ts = jsonObject.getAsJsonPrimitive("ts").getAsString();
            String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();
            String lat = location.getAsJsonPrimitive("lat").getAsString();
            String lon = location.getAsJsonPrimitive("lon").getAsString();

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/" + topicName + ".db")) {
                String tableName = name.toLowerCase().replace(" ", "_");
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "name TEXT," +
                        "clouds TEXT," +
                        "windSpeed TEXT," +
                        "temperature TEXT," +
                        "humidity TEXT," +
                        "instant TEXT," +
                        "precipitation TEXT," +
                        "ts TEXT," +
                        "ss TEXT," +
                        "lat TEXT," +
                        "lon TEXT" +
                        ")";

                System.out.println("Created table for: " + tableName);
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createTableSQL);

                    String insertWeatherSQL = "INSERT INTO " + tableName + " (name, clouds, windSpeed, temperature, humidity, instant, precipitation, ts, ss, lat, lon) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertWeatherSQL)) {
                        insertStatement.setString(1, name);
                        insertStatement.setString(2, clouds);
                        insertStatement.setString(3, windSpeed);
                        insertStatement.setString(4, temperature);
                        insertStatement.setString(5, humidity);
                        insertStatement.setString(6, instant);
                        insertStatement.setString(7, precipitation);
                        insertStatement.setString(8, ts);
                        insertStatement.setString(9, ss);
                        insertStatement.setString(10, lat);
                        insertStatement.setString(11, lon);

                        insertStatement.executeUpdate();
                    }
                }
                System.out.println("Base de datos para: " + topicName);
            } catch (SQLException | NullPointerException exc) {
                exc.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


