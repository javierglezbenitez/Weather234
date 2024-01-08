package dacd.gonzalez;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class UserInterface {
    private static final String URL = "jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/datamart.db";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("Welcome to the Reservation System!");

        // Check-in will always be today until 12:00
        Instant currentInstant = Instant.now();
        Instant checkInInstant = currentInstant.isAfter(currentInstant.truncatedTo(ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS))
                ? currentInstant.truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS)
                : currentInstant.truncatedTo(ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS);

        String checkIn = getFormattedDate(checkInInstant);
        // Lógica para obtener un check-in del día siguiente y sumar un día al check-out


        // Ask the user about the island they want to visit
        String chosenCountry = chosenCountry();
        String checkOutDate = chooseReservationDate("check-out");

        // Calculate the duration of the reservation
        System.out.println("Chosen country: " + chosenCountry);

        // Get and display the average meteorological data for the reservation period
        displayAverageWeatherData(chosenCountry, checkOutDate);

        recomendarHoteles(chosenCountry);


    }
    private static void displayAverageWeatherData(String country, String checkOutDate) {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT clouds, windSpeed, temperature, humidity, precipitation " +
                             "FROM Weather " +
                             "WHERE name = ? AND instant = ?")) {

            statement.setString(1, country);
            statement.setString(2, checkOutDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String clouds = resultSet.getString("clouds");
                    String windSpeed = resultSet.getString("windSpeed");
                    String temperature = resultSet.getString("temperature");
                    String humidity = resultSet.getString("humidity");
                    String precipitation = resultSet.getString("precipitation");

                    // Mostrar los datos meteorológicos al usuario
                    System.out.println("Datos meteorológicos para " + country +
                            " el " + checkOutDate + ":");
                    System.out.println("Nubosidad: " + clouds);
                    System.out.println("Velocidad del viento: " + windSpeed);
                    System.out.println("Temperatura: " + temperature);
                    System.out.println("Humedad: " + humidity);
                    System.out.println("Probabilidad de precipitación: " + precipitation);
                    System.out.println("----------------------------------------");
                } else {
                    System.out.println("No hay datos meteorológicos disponibles para la fecha especificada.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static String chooseReservationDate(String type) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select the " + type + " date:");
        System.out.println("1. In 1 day");
        System.out.println("2. In 2 days");
        System.out.println("3. In 3 days");
        System.out.println("4. In 4 days");

        int option = getUserOption();

        Instant reservationDate;
        switch (option) {

            case 1:
                reservationDate = Instant.now().plus(1, ChronoUnit.DAYS);
                break;
            case 2:
                reservationDate = Instant.now().plus(2, ChronoUnit.DAYS);
                break;
            case 3:
                reservationDate = Instant.now().plus(3, ChronoUnit.DAYS);
                break;
            case 4:
                reservationDate = Instant.now().plus(4, ChronoUnit.DAYS);
                break;
            default:
                System.out.println("Invalid option. Selecting today by default.");
                reservationDate = Instant.now();
        }

        String formattedDate = formatInstantAsDate(reservationDate);
        System.out.println(type + " date selected: " + formattedDate);

        return formattedDate;
    }


    private static int getUserOption() {
        System.out.print("Enter the option number: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static String chosenCountry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select the city you want to visit:");
        System.out.println("1. Madrid");
        System.out.println("2. Duabi");
        System.out.println("3. Paris");
        System.out.println("4. Bang Kho Laem");
        System.out.println("5. New York");
        System.out.println("6. Amsterdam");
        System.out.println("7. Nairobi");
        System.out.println("8. Milan");

        int option = getUserOption();

        switch (option) {
            case 1:
                return "Madrid";
            case 2:
                return "Duabi";
            case 3:
                return "Paris";
            case 4:
                return "Bang Kho Laem";
            case 5:
                return "New York";
            case 6:
                return "Amsterdam";
            case 7:
                return "Nairobi";
            case 8:
                return "Milan";
            default:
                System.out.println("Error. Any city has been selected.");
        }return null;
    }

    private static String formatInstantAsDate(Instant instant) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(dateFormatter);
    }

    private static void recomendarHoteles(String country) {
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT DISTINCT rate, tax, checkIn, checkOut, hotelName, rateName FROM Hotel WHERE location = ? ORDER BY rate DESC LIMIT 5")) {

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
    private static String getFormattedDate(Instant instant) {
        return Instant.ofEpochMilli(instant.toEpochMilli())
                .atZone(ZoneId.systemDefault())
                .format(dateFormatter);
    }
}