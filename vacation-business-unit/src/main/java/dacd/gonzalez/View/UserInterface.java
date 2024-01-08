package dacd.gonzalez.View;

import dacd.gonzalez.Model.Command;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class UserInterface {
    public Command command;

    public UserInterface(Command command) {
        this.command = command;
    }

    public void execute() {
        System.out.println("Welcome to the Reservation System!");

        LocalDate checkInWeather = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(12, 0))) {
            checkInWeather = checkInWeather.plusDays(1);
        }

        String checkinWeather = getFormattedDate(checkInWeather);

        // Lógica para obtener un check-in del día siguiente y sumar un día al check-out


        // Ask the user about the island they want to visit
        String chosenCountry = chosenCountry();
        String checkOutDate = chooseReservationDate("check-out");

        // Calculate the duration of the reservation
        System.out.println("Chosen country: " + chosenCountry);

        // Get and display the average meteorological data for the reservation period
        command.displayAverageWeatherData(chosenCountry, checkinWeather, checkOutDate);

        command.recomendarHoteles(chosenCountry);

        // Display the main menu or perform other actions as needed
        // ...
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

        System.out.println("Select the country you want to visit:");
        System.out.println("1. Madrid");
        System.out.println("2. Barcelona");
        System.out.println("3. Sevilla");
        System.out.println("4. Valencia");
        System.out.println("5. Vigo");
        System.out.println("6. Cádiz");
        System.out.println("7. Pamplona");
        System.out.println("8. Málaga");

        int option = getUserOption();

        switch (option) {
            case 1:
                return "Madrid";
            case 2:
                return "Barcelona";
            case 3:
                return "Seville";
            case 4:
                return "Valencia";
            case 5:
                return "Vico";
            case 6:
                return "Cadiz";
            case 7:
                return "Pamplona";
            case 8:
                return "Málaga";
            default:
                System.out.println("Invalid option. Selecting Madrid by default.");
                return "Madrid";
        }
    }

    private static String formatInstantAsDate(Instant instant) {
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private static String getFormattedDate(LocalDate instant) {
        return instant.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}