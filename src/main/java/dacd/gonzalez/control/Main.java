package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Main {

    public static void main(String[] args) {

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                WeatherController weatherController = new WeatherController(new MapWeatherProvider(args[0]));
                weatherController.execute();
            }
        };

        long time_of_execution = 6 * 60 * 60 * 1000;
        timer.schedule(timerTask, 0, time_of_execution);
    }


}

