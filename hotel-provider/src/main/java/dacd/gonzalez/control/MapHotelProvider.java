package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Hotel;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Rate;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MapHotelProvider implements HotelProvider {
    @Override
    public Hotel getHotel(Location location) {
        Hotel hotel = null;
        try {
            String apiUrl = "https://data.xotelo.com/api/rates?hotel_key=" + location.getHotelKey() +
                    "&chk_in=" + location.getCheckIn() + "&chk_out=" + location.getCheckOut();

            String jsonString = Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();
            JsonObject responseJson = new Gson().fromJson(jsonString, JsonObject.class);

            JsonObject resultObject = responseJson.getAsJsonObject("result");

            if (resultObject != null) {
                String checkInDate = resultObject.getAsJsonPrimitive("chk_in").getAsString();
                String checkOutDate = resultObject.getAsJsonPrimitive("chk_out").getAsString();
                Location location1 = new Location(checkInDate, checkOutDate, location.getHotelKey(), location.getName(), location.getLocation());

                long dt = responseJson.get("timestamp").getAsLong();
                Instant instant = Instant.ofEpochMilli(dt);

                JsonArray ratesArray = resultObject.getAsJsonArray("rates");
                ArrayList<Rate> rates = new ArrayList<>();

                for (JsonElement element : ratesArray) {
                    JsonObject rateObject = element.getAsJsonObject();
                    String code = rateObject.getAsJsonPrimitive("code").getAsString();
                    String name = rateObject.getAsJsonPrimitive("name").getAsString();
                    int rateValue = rateObject.getAsJsonPrimitive("rate").getAsInt();
                    int tax = rateObject.getAsJsonPrimitive("tax").getAsInt();

                    Rate rate = new Rate(code, name, rateValue, tax);
                    rates.add(rate);
                }

                hotel = new Hotel(rates, location1, instant);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }return hotel;
    }
}
