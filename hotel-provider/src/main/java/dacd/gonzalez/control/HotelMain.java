package dacd.gonzalez.control;

import java.util.Timer;
import java.util.TimerTask;

public class HotelMain {
    private static String topicName = "prediction.Hotel";


    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HotelProvider hotelProvider =  new MapHotelProvider();
                HotelStore hotelStore = new JmsHotelStore(args[0], topicName);
                HotelController hotelController = new HotelController(hotelProvider,hotelStore);
                hotelController.execute();
            }
        };

        long time_of_execution = 21600 * 1000;
        timer.schedule(timerTask, 0, time_of_execution);
    }
}
