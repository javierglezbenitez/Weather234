package dacd.gonzalez.Model;

import java.sql.*;
import java.time.LocalDate;

public class CommandBuilder {
    private static final String URL = "jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/datamart.db";


    public void displayAverageWeatherData(String country, String checkInDate, String checkOutDate) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT AVG(clouds) AS meanclouds, AVG(windSpeed) AS meanWindspeed, AVG(temperature) AS meanTemperature, AVG(humidity) AS meanHumidity, AVG(precipitation) AS meanPrecipitations " +
                            "FROM Weather " +
                            "WHERE name = ? AND instant BETWEEN ? AND ?");

            statement.setString(1, country);
            statement.setString(2, checkInDate);
            statement.setString(3, checkOutDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double avgClouds = resultSet.getDouble("meanclouds");
                    double avgWind = resultSet.getDouble("meanWindspeed");
                    double avgTemperature = resultSet.getDouble("meanTemperature");
                    double avgHumidity = resultSet.getDouble("meanHumidity");
                    double avgPop = resultSet.getDouble("meanPrecipitations");

                    System.out.println("Average weather data for " + country +
                            " during the reservation period (" + checkInDate +
                            " to " + checkOutDate + "):");
                    System.out.println("-Average Cloudiness: " + String.format("%.2f", avgClouds));
                    System.out.println("-Average Wind Speed: " + String.format("%.2f", avgWind));
                    System.out.println("-Average Temperature: " + String.format("%.2f", avgTemperature));
                    System.out.println("-Average Humidity: " + String.format("%.2f", avgHumidity));
                    System.out.println("-Average Probability of Precipitation: " + String.format("%.2f", avgPop));
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

    public void recommendedHotels(String country, String checkIn, String checkOut) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);

            int dias = (int) checkInDate.until(checkOutDate).getDays();

            try (Connection connection = DriverManager.getConnection(URL);
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT DISTINCT rate, tax, hotelName, rateName "
                                 + "FROM Hotel "
                                 + "WHERE location = ? AND checkIn = ?")) {

                statement.setString(1, country);
                statement.setString(2, checkIn);

                ResultSet resultSet = statement.executeQuery();

                int mejorPrecio = Integer.MAX_VALUE;
                String mejorHotel = null;
                String mejorRateName = null;
                int mejorRate = 0;
                int mejorTax = 0;

                while (resultSet.next()) {
                    int rate = resultSet.getInt("rate");
                    int tax = resultSet.getInt("tax");
                    String hotelName = resultSet.getString("hotelName");
                    String rateName = resultSet.getString("rateName");

                    int precioTotal = (rate * dias) + tax;

                    if (precioTotal < mejorPrecio) {
                        mejorPrecio = precioTotal;
                        mejorHotel = hotelName;
                        mejorRateName = rateName;
                        mejorRate = rate;
                        mejorTax = tax;

                    }
                }

                if (mejorHotel != null) {
                    System.out.println("Recommended hotel in " + country + ":");
                    System.out.println("Price per night: " + mejorRate);
                    System.out.println("Taxes for the stay: " + mejorTax);
                    System.out.println("Total price: " + mejorPrecio);
                    System.out.println("CheckIn: " + checkIn);
                    System.out.println("CheckOut: " + checkOut);
                    System.out.println("Number of days for stay: " + dias);
                    System.out.println("Hotel name: " + mejorHotel);
                    System.out.println("Web page: " + mejorRateName);
                    System.out.println("----------------------------------------");
                } else {
                    System.out.println("There are no hotels available for the specified date.");
                }
            }

        } catch (SQLException | java.time.format.DateTimeParseException e) {
            e.printStackTrace();
        }
    }
}
