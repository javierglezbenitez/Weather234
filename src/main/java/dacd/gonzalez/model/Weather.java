package dacd.gonzalez.model;

import java.time.Instant;

public class Weather {
    private static double temp;
    private static int humidity;
    private static int all;
    private static double speed;
    private static Double pop;
    private static Instant dt;

    public Weather(double temp, int humidity, int all, double speed, Double pop, Instant dt) {
        this.temp = temp;
        this.humidity = humidity;
        this.all = all;
        this.speed = speed;
        this.pop = pop;
        this.dt = dt;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getCloud() {
        return all;
    }

    public double getSpeed() {
        return speed;
    }

    public Double getPop() {
        return pop;
    }

    public Instant getInstant() {
        return dt;
    }
}
