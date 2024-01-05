package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.*;

public class HotelDatamart implements HotelStorer {

    @Override
    public void storeHotel(String event, String topicName) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            JsonArray ratesArray = jsonObject.getAsJsonArray("rates");
            JsonObject dateObject = jsonObject.getAsJsonObject("date");
            String location  = dateObject.getAsJsonPrimitive("location").getAsString();

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/"+ topicName + ".db")) {
                String tableName = location.toLowerCase().replace(" ", "_");
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

                    for (JsonElement rateElement : ratesArray) {
                        JsonObject rateObject = rateElement.getAsJsonObject();
                        String code = rateObject.getAsJsonPrimitive("code").getAsString();
                        String rateName = rateObject.getAsJsonPrimitive("rateName").getAsString();
                        String rate = rateObject.getAsJsonPrimitive("rate").getAsString();
                        String tax = rateObject.getAsJsonPrimitive("tax").getAsString();

                        String insertRateSQL = "INSERT INTO " + tableName + " (hotelName, hotelKey, checkIn, checkOut, ss, ts, code, rateName, rate, tax) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertRateSQL)) {
                            insertStatement.setString(1, dateObject.getAsJsonPrimitive("name").getAsString());
                            insertStatement.setString(2, dateObject.getAsJsonPrimitive("hotelKey").getAsString());
                            insertStatement.setString(3, dateObject.getAsJsonPrimitive("checkIn").getAsString());
                            insertStatement.setString(4, dateObject.getAsJsonPrimitive("checkOut").getAsString());
                            insertStatement.setString(5, jsonObject.getAsJsonPrimitive("ss").getAsString());
                            insertStatement.setString(6, jsonObject.getAsJsonPrimitive("ts").getAsString());
                            insertStatement.setString(7, code);
                            insertStatement.setString(8, rateName);
                            insertStatement.setString(9, rate);
                            insertStatement.setString(10, tax);

                            insertStatement.executeUpdate();
                        }
                    }
                }
                System.out.println("Base de datos para: " + topicName );
            } catch (SQLException | NullPointerException exc) {
                exc.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
