package dacd.gonzalez.model;

import java.time.Instant;

public class Weather {
    private final double temperature;
    private final int humidity;
    private final int clouds;
    private final double windSpeed;
    private final Double precipitation;
    private final Instant dt;

    private  final Instant ts;
    private  final String ss;


    public Weather(double temperature, int humidity, int rain, double windSpeed, Double precipitation, Instant dt) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.clouds = rain;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.dt = dt;
        this.ts = Instant.now();
        this.ss = "prediction-provider";

    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public Instant getDt() {
        return dt;
    }

    public Instant getInstant() {
        return dt;
    }

    public Instant getTs(){return ts;}
    public String getSs(){return ss;}

}
