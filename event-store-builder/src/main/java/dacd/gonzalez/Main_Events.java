package dacd.gonzalez;

import java.util.Timer;
import java.util.TimerTask;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main_Events {
    private static String subscriberName = "MyDurableSubscriber";
    public  static void main(String[] args) {

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                WeatherReciever weatherReciever = new JmrWeatherStore(args[0], args[1], subscriberName);
                weatherReciever.receiveBrokerMessage();

            }
        };

        long time_of_execution = 21600 * 1000;
        timer.schedule(timerTask, 0, time_of_execution);
    }
}