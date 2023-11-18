package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import java.sql.*;
import java.time.Instant;


public class SqliteWeatherStore implements WeatherStore{
    @Override
    public void save(Location location, Instant instant) {
        WeatherProvider weatherProvider = new MapWeatherProvider(MapWeatherProvider.getApiKey());
        Weather weather = weatherProvider.WeatherGet(location, instant);

        if (weather != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/Weather.db");

                String tableName =  location.getName().toLowerCase().replace(" ", "_");
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "temperature REAL," +
                        "humidity INTEGER," +
                        "clouds INTEGER," +
                        "wind REAL," +
                        "pop REAL," +
                        "instant TEXT" +
                        ")";

                System.out.println("Created table for " + location.getName());
                Statement statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);

                if (!CheckIfExistData(connection, tableName, instant.toString())) {
                    String insertWeatherSQL = "INSERT INTO " + tableName + " (temperature, humidity, clouds, wind, pop, instant)" +
                            " VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertWeatherSQL);

                    insertStatement.setDouble(1, weather.getTemp());
                    insertStatement.setInt(2, weather.getHumidity());
                    insertStatement.setInt(3, weather.getCloud());
                    insertStatement.setDouble(4, weather.getSpeed());
                    insertStatement.setDouble(5, weather.getPop());
                    insertStatement.setString(6, instant.toString());

                    insertStatement.executeUpdate();
                } else {
                    String updateWeatherSQL = "UPDATE " + tableName + " SET temperature=?, humidity=?, clouds=?, wind=?, pop=? " +
                            "WHERE instant=?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateWeatherSQL);

                    updateStatement.setDouble(1, weather.getTemp());
                    updateStatement.setInt(2, weather.getHumidity());
                    updateStatement.setInt(3, weather.getCloud());
                    updateStatement.setDouble(4, weather.getSpeed());
                    updateStatement.setDouble(5, weather.getPop());
                    updateStatement.setString(6, instant.toString());

                    updateStatement.executeUpdate();
                }

                connection.close();

            } catch (SQLException | NullPointerException exc) {
                exc.printStackTrace();
            }
        } else {
            System.out.println("Next island is " + location.getName());
        }
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
