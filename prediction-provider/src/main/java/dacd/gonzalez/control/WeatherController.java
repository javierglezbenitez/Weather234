package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherController{

    private WeatherProvider weatherProvider;
    private WeatherStore weatherStore;
    public WeatherController(WeatherProvider weatherProvider, WeatherStore weatherStore){
        this.weatherProvider = weatherProvider;
        this.weatherStore = weatherStore;
    }



    public void execute(){
        Location lanzarote = new Location("Lanzarote", 28.96302, -13.54769);
        Location fuerteventura = new Location("Fuerteventura", 28.50038, -13.86272);
        Location GranCanaria = new Location("Gran Canaria", 28.09973, -15.41343);
        Location tenerife = new Location("Tenerife", 28.46824, -16.25462);
        Location hierro = new Location("Hierro", 27.80628, -17.91578);
        Location gomera = new Location("Gomera", 28.09163, -17.11331);
        Location palma = new Location("Palma", 28.68351, -17.76421);
        Location graciosa = new Location("Graciosa", 29.23147, -13.50341);

        List<Location> islands = List.of(lanzarote, fuerteventura, GranCanaria, tenerife, hierro, gomera, palma, graciosa);

        ArrayList<Weather> weathers = new ArrayList<>();
        ArrayList<Instant> instants = new ArrayList<>();


        instantCreated(instants);
        callWeatherGet(instants, islands, weathers);
        callStored(instants, islands);


    }

    public  ArrayList<Weather> callWeatherGet(ArrayList<Instant> instants, List<Location> islands, ArrayList<Weather> weathers) {
        for (Location iteredLocation : islands) {
            for (Instant iteredInstant : instants) {
                Weather weather = weatherProvider.getWeather(iteredLocation, iteredInstant);
                weathers.add(weather);
            }
        }
        return weathers;
    }


    public  ArrayList<Instant> instantCreated(ArrayList<Instant> instants) {
        for (int i = 0; i < 5; i++) {
            LocalDate today = LocalDate.now();
            LocalTime hour = LocalTime.of(12, 0);
            LocalDateTime todayHour = LocalDateTime.of(today, hour);
            Instant previusInstant = todayHour.toInstant(ZoneOffset.UTC);
            Instant nextInstant = previusInstant.plus(i, ChronoUnit.DAYS);
            instants.add(nextInstant);
        }
        return instants;
    }

    public  void callStored(ArrayList<Instant> instants, List<Location> islands) {
        for (Location iteredLocation : islands) {
            for (Instant iteredInstant : instants) {
                weatherStore.save(iteredLocation, iteredInstant);
            }
        }
    }

    }