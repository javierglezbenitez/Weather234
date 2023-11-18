package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

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
        Location hierro = new Location("Hierro", 27.80628, -17.91578);
        Location gomera = new Location("Gomera", 28.09163, -17.11331);
        Location palma = new Location("Palma", 28.68351, -17.76421);
        Location graciosa = new Location("Graciosa", 29.23147, -13.50341);

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

        InstantCreated(instants);
        CallWeatherGet(instants, islands, weathers);
        CallStored(instants, islands);

    }

    public static ArrayList<Weather> CallWeatherGet(ArrayList<Instant> instants, ArrayList<Location> islands, ArrayList<Weather> weathers) {
        WeatherProvider weatherProvider = new MapWeatherProvider(MapWeatherProvider.getApiKey());
        for (Location iteredLocation : islands) {
            for (Instant iteredInstant : instants) {
                Weather weather = weatherProvider.WeatherGet(iteredLocation, iteredInstant);
                weathers.add(weather);
            }
        }
        return weathers;
    }

    public static void CallStored(ArrayList<Instant> instants, ArrayList<Location> islands) {
        for (Location iteredLocation : islands) {
            WeatherStore weatherStore = new SqliteWeatherStore();
            for (Instant iteredInstant : instants) {
                weatherStore.save(iteredLocation, iteredInstant);
            }
        }
    }
    public static ArrayList<Instant> InstantCreated(ArrayList<Instant> instants) {
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

    public static void main(String[] args) {
        WeatherController weatherController = new WeatherController( new MapWeatherProvider(MapWeatherProvider.getApiKey()));
        weatherController.execute();
    }
}