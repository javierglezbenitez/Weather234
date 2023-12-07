package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapWeatherProvider implements WeatherProvider {
    private static String API_KEY;


    public  MapWeatherProvider(String API_KEY) {
        this.API_KEY = API_KEY;

    }

    public static String getApiKey() {
        return API_KEY;
    }


    @Override
    public  Weather getWeather(Location location, Instant instant) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() + "&lon=" + location.getLon() + "&appid=" + API_KEY;
            JsonObject weathers = new Gson().fromJson(Jsoup.connect(apiUrl).ignoreContentType(true).execute().body(), JsonObject.class);
            JsonArray listArray = weathers.getAsJsonArray("list");

            List<JsonElement> listElements = new ArrayList<>();

            listArray.forEach(listElements::add);
            return listElements.stream()
                    .map(JsonElement::getAsJsonObject)
                    .filter(weather -> {
                        long dt = weather.getAsJsonPrimitive("dt").getAsLong();
                        return Instant.ofEpochSecond(dt).equals(instant);
                    })
                    .findFirst()
                    .map(weather -> {
                        JsonObject city = weathers.getAsJsonObject("city");
                        String name = city.getAsJsonPrimitive("name").getAsString();
                        double lat = city.getAsJsonObject("coord").getAsJsonPrimitive("lat").getAsDouble();
                        double lon = city.getAsJsonObject("coord").getAsJsonPrimitive("lon").getAsDouble();

                        JsonObject main = weather.getAsJsonObject("main");
                        JsonObject clouds = weather.getAsJsonObject("clouds");
                        JsonObject wind = weather.getAsJsonObject("wind");

                        double temp = main.getAsJsonPrimitive("temp").getAsDouble();
                        int humidity = main.getAsJsonPrimitive("humidity").getAsInt();
                        int all = clouds.getAsJsonPrimitive("all").getAsInt();
                        double speed = wind.getAsJsonPrimitive("speed").getAsDouble();
                        double pop = weather.getAsJsonPrimitive("pop").getAsDouble();
                        Instant weatherInstant = Instant.ofEpochSecond(weather.getAsJsonPrimitive("dt").getAsLong());


                        Location cityLocation = new Location(name, lat, lon);

                        return new Weather(cityLocation, temp, humidity, all, speed, pop, weatherInstant);
                    })
                    .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}