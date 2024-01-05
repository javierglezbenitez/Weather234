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
import java.util.Date;

public class MapHotelProvider implements HotelProvider {
    @Override
    public Hotel getHotel(Location location) {
        Hotel hotel = null;

        try {
            String apiUrl = "https://data.xotelo.com/api/rates?hotel_key=" + location.getHotelKey() +
                    "&chk_in=" + location.getCheckIn() + "&chk_out=" + location.getCheckOut();

            String jsonString = Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();
            JsonObject responseJson = new Gson().fromJson(jsonString, JsonObject.class);

            JsonElement resultElement = responseJson.get("result");

            if (resultElement.isJsonObject()) {
                JsonObject resultObject = resultElement.getAsJsonObject();
                String checkInDate = resultObject.getAsJsonPrimitive("chk_in").getAsString();
                String checkOutDate = resultObject.getAsJsonPrimitive("chk_out").getAsString();
                Location date1 = new Location(checkInDate, checkOutDate, location.getHotelKey(), location.getName(), location.getLocation());

                JsonArray ratesObject = resultObject.getAsJsonArray("rates");

                ArrayList<Rate> rates = new ArrayList<>();

                for (JsonElement rate : ratesObject) {
                    JsonObject rateObject = rate.getAsJsonObject();

                    String code = rateObject.getAsJsonPrimitive("code").getAsString();
                    String name = rateObject.getAsJsonPrimitive("name").getAsString();
                    int rateName = rateObject.getAsJsonPrimitive("rate").getAsInt();
                    int tax = rateObject.getAsJsonPrimitive("tax").getAsInt();

                    Rate rate1 = new Rate(code,name,rateName,tax);

                    rates.add(rate1);

                }hotel = new Hotel(rates,date1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }return hotel;
    }
}
