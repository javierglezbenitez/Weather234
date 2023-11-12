package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import org.jsoup.Jsoup;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

public interface WeatherProvider {
     Weather WeatherGet(Location location);

    }







