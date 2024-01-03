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
        Location parís = new Location("París", 48.8566, 2.3522);
        Location dubai = new Location("Dubai", 25.2769, 55.2963);
        Location españa = new Location("España", 40.41, -3.70);
        Location tailandia = new Location("Tailandia", 13.7, 100.5);
        Location nuevaYork = new Location("Nueva York", 40.7128, -74.0060);
        Location amsterdam = new Location("Amsterdam", 52.3676, 4.9041);
        Location nairobi = new Location("Nairobi", -1.2864, 36.8172);
        Location milan = new Location("Milan", 45.4642, 9.1900);

        List<Location> locations = List.of(parís, dubai, españa, tailandia, nuevaYork, amsterdam, nairobi, milan);

        ArrayList<Weather> weathers = new ArrayList<>();
        ArrayList<Instant> instants = new ArrayList<>();


        instantCreated(instants);
        callWeatherGet(instants, locations, weathers);
        callStored(instants, locations);


    }

    public  ArrayList<Weather> callWeatherGet(ArrayList<Instant> instants, List<Location> locations, ArrayList<Weather> weathers) {
        for (Location iteredLocation : locations) {
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

    public  void callStored(ArrayList<Instant> instants, List<Location> locations) {
        for (Location iteredLocation : locations) {
            for (Instant iteredInstant : instants) {
                weatherStore.send(weatherProvider.getWeather(iteredLocation, iteredInstant));
            }
        }
    }

    }