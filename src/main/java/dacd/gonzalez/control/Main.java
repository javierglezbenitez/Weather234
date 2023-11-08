package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Weather;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            String url = "https://api.openweathermap.org/data/2.5/forecast?lat=50.5456&lon=-15.41915&appid=51cb3b621914382d96a450c0f3451582";
            String jsonString = Jsoup.connect(url).ignoreContentType(true).execute().body();

            Gson gson = new Gson();
            JsonObject weathers = gson.fromJson(jsonString, JsonObject.class);
            JsonArray lists = weathers.getAsJsonObject().getAsJsonArray("list");

            ArrayList<Weather> weatherList = new ArrayList<>();

            for (JsonElement list: lists) {
                JsonObject weather = list.getAsJsonObject();
                JsonObject main = weather.get("main").getAsJsonObject();
                JsonObject clouds = weather.get("clouds").getAsJsonObject();
                JsonObject wind = weather.get("wind").getAsJsonObject();

                double temp = main.get("temp").getAsDouble();
                int humidity = main.get("humidity").getAsInt();
                int all = clouds.get("all").getAsInt();
                double speed = wind.get("speed").getAsDouble();
                Double pop = weather.get("pop").getAsDouble();
                int dt = weather.get("dt").getAsInt();

                long unixTimestamp = dt;
                Instant weatherInstant = Instant.ofEpochSecond(unixTimestamp);

                Weather weatherObject = new Weather(temp, humidity, all, speed, pop, weatherInstant);
                weatherList.add(weatherObject);



            }

            for (Weather weatherIter : weatherList) {
                System.out.println("Temp:"+weatherIter.getTemp());
                System.out.println("clouds:"+weatherIter.getAll());
                System.out.println("wind:"+weatherIter.getSpeed());
                System.out.println("humidity:"+weatherIter.getHumidity());
                System.out.println("pop:"+weatherIter.getPop());
                System.out.println("dt:"+weatherIter.getDt()+"\n");
            }

        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}