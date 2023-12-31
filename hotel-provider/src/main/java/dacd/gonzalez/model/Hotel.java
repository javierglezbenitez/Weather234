package dacd.gonzalez.model;

import java.time.Instant;
import java.util.ArrayList;

public class Hotel {

    ArrayList<Rate> rates;

    private final Location date;

    private  final String ss;
    private Instant timeStamp;
    private Instant ts;



    public Hotel(ArrayList<Rate> rates, Location date,Instant timeStamp) {
        this.rates = rates;
        this.date = date;
        this.ss = "prediction-provider";
        this.timeStamp=timeStamp;
        this.ts = Instant.now();
    }



    public Instant getTimeStamp() {
        return timeStamp;
    }

    public String getSs() {
        return ss;
    }
    public Location getDate() {
        return date;
    }
}

