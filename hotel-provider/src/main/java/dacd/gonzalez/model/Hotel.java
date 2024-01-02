package dacd.gonzalez.model;

import java.time.Instant;
import java.util.ArrayList;

public class Hotel {

    ArrayList<Rate> rates;

    private final Location date;

    private  final String ss;
    private Instant ts;



    public Hotel(ArrayList<Rate> rates, Location date) {
        this.rates = rates;
        this.date = date;
        this.ss = "hotel-provider";
        this.ts = Instant.now();
    }




    public String getSs() {
        return ss;
    }
    public Location getDate() {
        return date;
    }
}

