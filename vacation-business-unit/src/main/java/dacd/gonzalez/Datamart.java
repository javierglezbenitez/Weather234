package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Datamart implements Storer {
    private String url;

    public Datamart(String url) {
        this.url = url;
    }

    @Override
    public void storeWeather(String event) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            JsonObject location = jsonObject.getAsJsonObject("location");
            String name = location.getAsJsonPrimitive("name").getAsString();
            String timestamp = jsonObject.getAsJsonPrimitive("ts").getAsString();
            String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();
            String lat = location.getAsJsonPrimitive("lat").getAsString();
            String lon = location.getAsJsonPrimitive("lon").getAsString();
            String temperature = jsonObject.getAsJsonPrimitive("temperature").getAsString();
            String humidity = jsonObject.getAsJsonPrimitive("humidity").getAsString();
            String clouds = jsonObject.getAsJsonPrimitive("clouds").getAsString();
            String windSpeed = jsonObject.getAsJsonPrimitive("windSpeed").getAsString();
            String precipitation = jsonObject.getAsJsonPrimitive("precipitation").getAsString();
            String instant = jsonObject.getAsJsonPrimitive("instant").getAsString();



            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault());
            String ts = dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm"));

            try (Connection connection = DriverManager.getConnection(url)) {
                crearTablaWeather(connection);

                connection.setAutoCommit(false);

                try {
                    String deleteRowsSQL = "DELETE FROM Weather WHERE ts <> ?";
                    try (PreparedStatement deleteRowsStatement = connection.prepareStatement(deleteRowsSQL)) {
                        deleteRowsStatement.setString(1, ts);
                        deleteRowsStatement.executeUpdate();
                    }

                    String insertWeatherSQL = "INSERT INTO Weather (name, clouds, windSpeed, temperature, humidity, instant, precipitation, ts, ss, lat, lon) " +
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

                    connection.commit();
                    System.out.println("Base de datos para Weather");
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



    private void crearTablaWeather(Connection connection) {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Weather (" +
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

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
                System.out.println("Created table for Weather");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }


    @Override
    public void storeHotel(String event) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            JsonArray ratesArray = jsonObject.getAsJsonArray("rates");
            JsonObject dateObject = jsonObject.getAsJsonObject("date");
            String location = dateObject.getAsJsonPrimitive("location").getAsString();
            String hotelName = dateObject.getAsJsonPrimitive("name").getAsString();
            String hotelkey = dateObject.getAsJsonPrimitive("hotelKey").getAsString();
            String checkIn = dateObject.getAsJsonPrimitive("checkIn").getAsString();
            String checkOut = dateObject.getAsJsonPrimitive("checkOut").getAsString();

            String timestamp = jsonObject.getAsJsonPrimitive("ts").getAsString();
            String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault());
            String ts = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"));

            try (Connection connection = DriverManager.getConnection(url)) {
                // Utilize topicName with a different naming convention or suffix for the second table
                crearTablaHotel(connection);

                connection.setAutoCommit(false);

                try {
                    String deleteRowsSQL = "DELETE FROM Hotel WHERE ts <> ?";
                    try (PreparedStatement deleteRowsStatement = connection.prepareStatement(deleteRowsSQL)) {
                        deleteRowsStatement.setString(1, ts);
                        deleteRowsStatement.executeUpdate();
                    }

                    for (JsonElement rateElement : ratesArray) {
                        JsonObject rateObject = rateElement.getAsJsonObject();
                        String code = rateObject.getAsJsonPrimitive("code").getAsString();
                        String rateName = rateObject.getAsJsonPrimitive("rateName").getAsString();
                        String rateValue = rateObject.getAsJsonPrimitive("rate").getAsString();
                        String taxValue = rateObject.getAsJsonPrimitive("tax").getAsString();

                        String insertRateSQL = "INSERT INTO Hotel (location, hotelName, hotelKey, checkIn, checkOut, ss, ts, code, rateName, rate, tax) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertRateSQL)) {
                            insertStatement.setString(1, location);
                            insertStatement.setString(2, hotelName);
                            insertStatement.setString(3, hotelkey);
                            insertStatement.setString(4, checkIn);
                            insertStatement.setString(5, checkOut);
                            insertStatement.setString(6, ss);
                            insertStatement.setString(7, ts);
                            insertStatement.setString(8, code);
                            insertStatement.setString(9, rateName);
                            insertStatement.setString(10, rateValue);
                            insertStatement.setString(11, taxValue);

                            insertStatement.executeUpdate();
                        }
                    }

                    connection.commit();
                    System.out.println("Base de datos para Hotel");
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
    private void crearTablaHotel(Connection connection) {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Hotel(" +
                    "location TEXT," +
                    "hotelName TEXT," +
                    "hotelKey TEXT," +
                    "checkIn TEXT," +
                    "checkOut TEXT," +
                    "ss TEXT," +
                    "ts TEXT," +
                    "code TEXT," +
                    "rateName TEXT," +
                    "rate INTEGER," +
                    "tax INTEGER" +
                    ")";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSQL);
                System.out.println("Created table for Hotel");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
