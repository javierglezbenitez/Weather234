package dacd.gonzalez.View;

import dacd.gonzalez.Model.CommandBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class UserInterface {
    public CommandBuilder commandBuilder;


    public UserInterface(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    public void execute() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                            We are TwinsCompany\n" +
                "                  Enjoy your best vacation to the fullest\n");
        LocalDate checkInWeather = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(12, 0))) {
            checkInWeather = checkInWeather.plusDays(1);
        }

        Instant checkInWeatherInstant = checkInWeather.atStartOfDay(ZoneId.systemDefault()).toInstant();

        String chosenCountry = chosenCountry();
        String checkOut = chooseReservationDate( checkInWeatherInstant);

        String checkIn = formatInstantAsDate(checkInWeatherInstant);

        System.out.println("Chosen city: " + chosenCountry);
        System.out.println("----------------------------------------------");

        commandBuilder.displayAverageWeatherData(chosenCountry, checkIn, checkOut);

        commandBuilder.recommendedHotels(chosenCountry, checkIn, checkOut);

        System.out.println("Reservation done Correctly\n"  );
        System.out.println("                            Enjoy your vacation. See you next 😉");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private  String chooseReservationDate( Instant checkInWeather) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("How many days you want to stay?: " );
        System.out.println("1.  1 day");
        System.out.println("2.  2 days");
        System.out.println("3.  3 days");
        System.out.println("4.  4 days");

        int option = getUserOption();

        Instant reservationDate;
        switch (option) {
            case 1:
                reservationDate = checkInWeather.plus(1, ChronoUnit.DAYS);
                break;
            case 2:
                reservationDate = checkInWeather.plus(2, ChronoUnit.DAYS);
                break;
            case 3:
                reservationDate = checkInWeather.plus(3, ChronoUnit.DAYS);
                break;
            case 4:
                reservationDate = checkInWeather.plus(4, ChronoUnit.DAYS);
                break;
            default:
                System.out.println("Invalid option. Today is selected by default.");
                reservationDate = checkInWeather;
        }

        String formattedDate = formatInstantAsDate(reservationDate);
        System.out.println("CheckOut selected: " + formattedDate);

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
        System.out.println("2. Dubai");
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
                return "Dubai";
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
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}