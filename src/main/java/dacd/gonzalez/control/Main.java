package dacd.gonzalez.control;

import java.util.Timer;
import java.util.TimerTask;

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

        long time_of_execution = 21600 * 1000;
        timer.schedule(timerTask, 0, time_of_execution);
    }


}

