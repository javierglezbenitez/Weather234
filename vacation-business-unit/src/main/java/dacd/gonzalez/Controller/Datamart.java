package dacd.gonzalez.Controller;

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
    public void WeatherStore(String event) {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(event, JsonObject.class);

            JsonObject locationObject = jsonObject.getAsJsonObject("location");
            String name = locationObject.get("name").getAsString();

            JsonObject location = jsonObject.getAsJsonObject("location");
            String timestamp = jsonObject.getAsJsonPrimitive("ts").getAsString();
            String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();
            double lat = location.get("lat").getAsDouble();
            double lon = location.get("lon").getAsDouble();
            double temperature = jsonObject.get("temperature").getAsDouble();
            int humidity = jsonObject.get("humidity").getAsInt();
            int clouds = jsonObject.get("clouds").getAsInt();
            double windSpeed = jsonObject.get("windSpeed").getAsDouble();
            double precipitation = jsonObject.get("precipitation").getAsDouble();
            String instant = jsonObject.get("instant").getAsString();



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
                        insertStatement.setInt(2, clouds);
                        insertStatement.setDouble(3, windSpeed);
                        insertStatement.setDouble(4, temperature);
                        insertStatement.setInt(5, humidity);
                        insertStatement.setString(6, instant);
                        insertStatement.setDouble(7, precipitation);
                        insertStatement.setString(8, ts);
                        insertStatement.setString(9, ss);
                        insertStatement.setDouble(10, lat);
                        insertStatement.setDouble(11, lon);

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
    public void HotelStore(String event) {
        try {
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(event, JsonObject.class);
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
                        int rateValue = rateObject.get("rate").getAsInt();
                        int taxValue = rateObject.get("tax").getAsInt();

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
                            insertStatement.setInt(10, rateValue);
                            insertStatement.setInt(11, taxValue);

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
