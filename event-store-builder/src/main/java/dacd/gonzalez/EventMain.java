package dacd.gonzalez;

import java.util.Timer;
import java.util.TimerTask;

public class EventMain {

        private static String subscriberName = "MySubscriber";
        public  static void main(String[] args) {

            Timer timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    EventStore weatherReciever = new JmrWeatherStore(args[0], args[1], subscriberName);
                    weatherReciever.receive();

                }
            };

            long time_of_execution = 21600 * 1000;
            timer.schedule(timerTask, 0, time_of_execution);
        }
}
