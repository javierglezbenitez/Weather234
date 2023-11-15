package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherController{

    public WeatherProvider weatherProvider;
    public WeatherController(WeatherProvider weatherProvider){
        this.weatherProvider = weatherProvider;
    }

    public void execute(){
        Location lanzarote = new Location("Lanzarote", 28.96302, -13.54769);
        Location fuerteventura = new Location("Fuerteventura", 28.50038, -13.86272);
        Location GranCanaria = new Location("Gran Canaria", 28.09973, -15.41343);
        Location tenerife = new Location("Tenerife", 28.46824, -16.25462);
        Location hierro = new Location("El hierro", 27.80628, -17.91578);
        Location gomera = new Location("La Gomera", 28.09163, -17.11331);
        Location palma = new Location("La Palma", 28.68351, -17.76421);
        Location graciosa = new Location("La Graciosa", 29.23147, -13.50341);

        ArrayList<Location> islands = new ArrayList<>();
        islands.add(lanzarote);
        islands.add(fuerteventura);
        islands.add(GranCanaria);
        islands.add(tenerife);
        islands.add(hierro);
        islands.add(gomera);
        islands.add(palma);
        islands.add(graciosa);



        ArrayList<Weather> weathers = new ArrayList<>();
        ArrayList<Instant> instants = new ArrayList<>();

        createInstant(instants);
        getWeatherCall(instants, islands, weathers);
        loadCall(instants, islands);

    }
    public static ArrayList<Instant> createInstant(ArrayList<Instant> instants) {
        for (int i = 0; i < 5; i++) {
            LocalDate hoy = LocalDate.now();
            LocalTime hour = LocalTime.of(12, 0);
            LocalDateTime todayHour = LocalDateTime.of(hoy, hour);
            Instant instant = todayHour.toInstant(ZoneOffset.UTC);
            Instant instant1 = instant.plus(i, ChronoUnit.DAYS);
            instants.add(instant1);
        }
        return instants;
    }

    public static ArrayList<Weather> getWeatherCall(ArrayList<Instant> instantList, List<Location> locationList,
                                                    ArrayList<Weather> weatherArrayList) {
        WeatherProvider weatherProvider = new MapWeatherProvider();

        for (Location iteredLocation : locationList) {
            for (Instant iteredInstant : instantList) {
                Weather weather = weatherProvider.WeatherGet(iteredLocation, iteredInstant);

                if (weather != null) {
                    System.out.println("Weather for " + iteredLocation.getName() + " at " + iteredInstant + ":");
                    System.out.println(weather);
                    System.out.println("\n");
                } else {
                    System.out.println("No weather data found for " + iteredLocation.getName() + " at " + iteredInstant);
                }
                weatherArrayList.add(weather);
            }
        }
        return weatherArrayList;
    }

    public static void loadCall(ArrayList<Instant> instantList, List<Location> locationList){
        WeatherStore weatherStore = new SqliteWeatherStore();
        for (Location iteredLocation : locationList) {
            for (Instant iteredInstant : instantList) {
                weatherStore.load(iteredLocation, iteredInstant);
            }
        }
    }

    public static void main(String[] args) {
        WeatherController weatherController = new WeatherController(new MapWeatherProvider());
        weatherController.execute();
    }
    }

