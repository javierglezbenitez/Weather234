package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class HotelDatamart implements HotelStorer {

    @Override
    public void storeHotel(String event, String topicName) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            JsonArray ratesArray = jsonObject.getAsJsonArray("rates");
            JsonObject dateObject = jsonObject.getAsJsonObject("date");
            String location = dateObject.getAsJsonPrimitive("location").getAsString();

            String timestamp = jsonObject.getAsJsonPrimitive("ts").getAsString();

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault());
            String ts = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"));

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/" + topicName + ".db")) {
                String tableName = location.toLowerCase().replace(" ", "_");
                crearTabla(connection, tableName);

                connection.setAutoCommit(false);

                try {
                    String deleteRowsSQL = "DELETE FROM " + tableName + " WHERE ts <> ?";
                    try (PreparedStatement deleteRowsStatement = connection.prepareStatement(deleteRowsSQL)) {
                        deleteRowsStatement.setString(1, ts);
                        deleteRowsStatement.executeUpdate();
                    }

                    for (JsonElement rateElement : ratesArray) {
                        JsonObject rateObject = rateElement.getAsJsonObject();
                        String code = getField(rateObject, "code");
                        String rateName = getField(rateObject, "rateName");
                        String rateValue = getField(rateObject, "rate");
                        String taxValue = getField(rateObject, "tax");

                        String insertRateSQL = "INSERT INTO " + tableName + " (hotelName, hotelKey, checkIn, checkOut, ss, ts, code, rateName, rate, tax) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertRateSQL)) {
                            insertStatement.setString(1, getField(dateObject, "name"));
                            insertStatement.setString(2, getField(dateObject, "hotelKey"));
                            insertStatement.setString(3, getField(dateObject, "checkIn"));
                            insertStatement.setString(4, getField(dateObject, "checkOut"));
                            insertStatement.setString(5, getField(jsonObject, "ss"));
                            insertStatement.setString(6, ts);
                            insertStatement.setString(7, code);
                            insertStatement.setString(8, rateName);
                            insertStatement.setString(9, rateValue);
                            insertStatement.setString(10, taxValue);

                            insertStatement.executeUpdate();
                        }
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
                System.out.println("Created table for: " + tableName);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
