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
import java.util.ArrayList;

public class MapHotelProvider implements HotelProvider {
    @Override
    public Hotel getHotel(Location location) {
        try {
            String apiUrl = "https://data.xotelo.com/api/rates?hotel_key=" + location.getHotelKey() +
                    "&chk_in=" + location.getCheckIn() + "&chk_out=" + location.getCheckOut();

            JsonObject responseJson = new Gson().fromJson(Jsoup.connect(apiUrl).ignoreContentType(true).execute().body(), JsonObject.class);
            JsonObject resultObject = responseJson.getAsJsonObject("result");

            Location date1 = new Location(resultObject.getAsJsonPrimitive("chk_in").getAsString(),
                    resultObject.getAsJsonPrimitive("chk_out").getAsString(),
                    location.getHotelKey(), location.getName(), location.getLocation());

            JsonArray ratesObject = resultObject.getAsJsonArray("rates");
            ArrayList<Rate> rates = new ArrayList<>();

            for (JsonElement rate : ratesObject) {
                JsonObject rateObject = rate.getAsJsonObject();
                rates.add(new Rate(rateObject.getAsJsonPrimitive("code").getAsString(),
                        rateObject.getAsJsonPrimitive("name").getAsString(),
                        rateObject.getAsJsonPrimitive("rate").getAsInt(),
                        rateObject.getAsJsonPrimitive("tax").getAsInt()));
            }

            return new Hotel(rates, date1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
