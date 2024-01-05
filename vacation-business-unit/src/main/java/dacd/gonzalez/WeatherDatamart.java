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
            String timestamp = jsonObject.getAsJsonPrimitive("ts").getAsString();

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault());
            String ts = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm"));

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/" + topicName +".db")) {
                String tableName = name.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");
                crearTabla(connection, tableName);

                connection.setAutoCommit(false);

                try {
                    String deleteRowsSQL = "DELETE FROM " + tableName + " WHERE ts <> ?";
                    try (PreparedStatement deleteRowsStatement = connection.prepareStatement(deleteRowsSQL)) {
                        deleteRowsStatement.setString(1, ts);
                        deleteRowsStatement.executeUpdate();
                    }

                    String insertWeatherSQL = "INSERT INTO " + tableName + " (clouds, windSpeed, temperature, humidity, instant, precipitation, ts, ss, lat, lon) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertWeatherSQL)) {
                        insertStatement.setString(1, getField(jsonObject, "clouds"));
                        insertStatement.setString(2, getField(jsonObject, "windSpeed"));
                        insertStatement.setString(3, getField(jsonObject, "temperature"));
                        insertStatement.setString(4, getField(jsonObject, "humidity"));
                        insertStatement.setString(5, getField(jsonObject, "instant"));
                        insertStatement.setString(6, getField(jsonObject, "precipitation"));
                        insertStatement.setString(7, ts);
                        insertStatement.setString(8, getField(jsonObject, "ss"));
                        insertStatement.setString(9, getField(location, "lat"));
                        insertStatement.setString(10, getField(location, "lon"));

                        insertStatement.executeUpdate();
                    }

                    connection.commit();
                    System.out.println("Base de datos para: " + topicName);
                } catch (SQLException exc) {
                    connection.rollback();
                    exc.printStackTrace();
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException | NullPointerException exc) {
                exc.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getField(JsonObject jsonObject, String field) {
        return jsonObject.getAsJsonPrimitive(field).getAsString();
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
