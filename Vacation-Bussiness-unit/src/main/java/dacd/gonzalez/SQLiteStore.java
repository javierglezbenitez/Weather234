package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;

public class SQLiteStore implements Storer {

    @Override
    public void store(String event) {
        try {
            JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
            String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();
            String ts = jsonObject.getAsJsonPrimitive("ts").getAsString();
            String temperature = jsonObject.getAsJsonPrimitive("temperature").getAsString();
            String humidity = jsonObject.getAsJsonPrimitive("humidity").getAsString();
            String clouds = jsonObject.getAsJsonPrimitive("clouds").getAsString();
            String windSpeed = jsonObject.getAsJsonPrimitive("windSpeed").getAsString();
            String precipitation = jsonObject.getAsJsonPrimitive("precipitation").getAsString();
            String instant = jsonObject.getAsJsonPrimitive("instant").getAsString();
            String name = jsonObject.getAsJsonPrimitive("name").getAsString();
            String lat = jsonObject.getAsJsonPrimitive("lat").getAsString();
            String lon = jsonObject.getAsJsonPrimitive("lon").getAsString();

            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/Weather.db");

            String tableName = name.toLowerCase().replace(" ", "_");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "clouds String," +
                    "windSpeed String," +
                    "temperature String," +
                    "humidity String," +
                    "instant String," +
                    "precipitation String" +
                    ")";

            System.out.println("Created table for " + name);
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);

            if (checkIfExistData(connection,tableName, instant.toString())){
                String updateWeatherSQL = "UPDATE " + tableName + " SET clouds=?, wind=?, temperature=?, humidity=?, pop=? " +
                        "WHERE instant=?";
                PreparedStatement updateStatement = connection.prepareStatement(updateWeatherSQL);

                updateStatement.setInt(1, weather.getCloud());
                updateStatement.setDouble(2, weather.getSpeed());
                updateStatement.setDouble(3, weather.getTemp());
                updateStatement.setInt(4, weather.getHumidity());
                updateStatement.setDouble(5, weather.getPop());
                updateStatement.setString(6, instant.toString());

                updateStatement.executeUpdate();
            } else{
                String insertWeatherSQL = "INSERT INTO " + tableName + " (clouds, wind, temperature, humidity, instant, pop)" +
                        " VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertWeatherSQL);

                insertStatement.setInt(1, weather.getCloud());
                insertStatement.setDouble(2, weather.getSpeed());
                insertStatement.setDouble(3, weather.getTemp());
                insertStatement.setInt(4, weather.getHumidity());
                insertStatement.setString(5, instant.toString());
                insertStatement.setDouble(6, weather.getPop());

                insertStatement.executeUpdate();
            }

            connection.close();

        } catch (SQLException | NullPointerException exc) {
            exc.printStackTrace();
        }
    } else

    {
        System.out.println("No weather data found for " + location.getName() + " at " + instant);
    }

    private static boolean CheckIfExistData(Connection connection, String tableName, String instant)
            throws SQLException {
        String formattedQuery = String.format("SELECT COUNT(*) FROM " + tableName + " WHERE instant = ?", tableName);

        try (PreparedStatement statement = connection.prepareStatement(formattedQuery)) {
            statement.setString(1, instant);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }
}
