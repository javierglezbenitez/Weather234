package dacd.gonzalez.Model;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class CommandBuilder implements Command{
    private static final String URL = "jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/datamart.db";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void displayAverageWeatherData(String country, String checkInDate, String checkOutDate) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT AVG(cloud) AS avg_clouds, AVG(windSpeed) AS avg_wind, AVG(temp) AS avg_temperature, AVG(humidity) AS avg_humidity, AVG(propRain) AS avg_pop " +
                            "FROM Weather " +
                            "WHERE name = ? AND instant BETWEEN ? AND ?");

            statement.setString(1, country);
            statement.setString(2, checkInDate);
            statement.setString(3, checkOutDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String avgClouds = resultSet.getString("avg_clouds");
                    String avgWind = resultSet.getString("avg_wind");
                    String avgTemperature = resultSet.getString("avg_temperature");
                    String avgHumidity = resultSet.getString("avg_humidity");
                    String avgPop = resultSet.getString("avg_pop");

                    // Display the average weather data to the user (you can adapt this code according to your needs)
                    System.out.println("Average weather data for " + country +
                            " during the reservation period (" + checkInDate +
                            " to " + checkOutDate + "):");
                    System.out.println("Average Cloudiness: " + avgClouds);
                    System.out.println("Average Wind Speed: " + avgWind);
                    System.out.println("Average Temperature: " + avgTemperature);
                    System.out.println("Average Humidity: " + avgHumidity);
                    System.out.println("Average Probability of Precipitation: " + avgPop);
                    System.out.println("----------------------------------------");
                } else {
                    System.out.println("f");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void recomendarHoteles(String country) {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT DISTINCT rate, tax, checkIn, checkOut, hotelName, rateName FROM Hotel WHERE country = ? ORDER BY rate DESC LIMIT 5")) {

            statement.setString(1, country);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Hoteles recomendados en " + country + ":");
                while (resultSet.next()) {
                    String rate = resultSet.getString("rate");
                    String tax = resultSet.getString("tax");
                    String checkIn = resultSet.getString("checkIn");
                    String checkOut = resultSet.getString("checkOut");
                    String hotelName = resultSet.getString("hotelName");
                    String rateName = resultSet.getString("rateName");

                    // Mostrar los datos del hotel recomendado al usuario
                    System.out.println("Tarifa: " + rate);
                    System.out.println("Impuesto: " + tax);
                    System.out.println("Fecha de entrada: " + checkIn);
                    System.out.println("Fecha de salida: " + checkOut);
                    System.out.println("Nombre del hotel: " + hotelName);
                    System.out.println("Nombre de la página Web: " + rateName);
                    System.out.println("----------------------------------------");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
