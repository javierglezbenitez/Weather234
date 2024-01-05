package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(ts), ZoneId.systemDefault());
            String ts1 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"));

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/" + topicName + ".db")) {
                String tableName = name.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");
                crearTabla(connection, tableName);

                // Iniciar una transacción
                connection.setAutoCommit(false);

                try {
                    // Eliminar filas con ts diferente al nuevo ts
                    String deleteRowsSQL = "DELETE FROM " + tableName + " WHERE ts <> ?";
                    try (PreparedStatement deleteRowsStatement = connection.prepareStatement(deleteRowsSQL)) {
                        deleteRowsStatement.setString(1, ts1);
                        deleteRowsStatement.executeUpdate();
                    }

                    // Inserción de nuevos datos
                    String insertWeatherSQL = "INSERT INTO " + tableName + " (clouds, windSpeed, temperature, humidity, instant, precipitation, ts, ss, lat, lon) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertWeatherSQL)) {
                        insertStatement.setString(1, clouds);
                        insertStatement.setString(2, windSpeed);
                        insertStatement.setString(3, temperature);
                        insertStatement.setString(4, humidity);
                        insertStatement.setString(5, instant);
                        insertStatement.setString(6, precipitation);
                        insertStatement.setString(7, ts1);
                        insertStatement.setString(8, ss);
                        insertStatement.setString(9, lat);
                        insertStatement.setString(10, lon);

                        insertStatement.executeUpdate();
                    }

                    // Confirmar la transacción
                    connection.commit();
                    System.out.println("Base de datos para: " + topicName);
                } catch (SQLException exc) {
                    // Si ocurre un error, hacer rollback de la transacción
                    connection.rollback();
                    exc.printStackTrace();
                } finally {
                    // Restaurar la configuración de autocommit
                    connection.setAutoCommit(true);
                }
            } catch (SQLException | NullPointerException exc) {
                exc.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearTabla(Connection connection, String tableName) {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
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

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
                System.out.println("Created table for: " + tableName);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
