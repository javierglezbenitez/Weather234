package dacd.gonzalez.model;

import java.time.Instant;
import java.util.ArrayList;

public class Hotel {

    private final ArrayList<Rate> rates;

    private final Location date;

    private  final String ss;
    private final Instant ts;



    public Hotel(ArrayList<Rate> rates, Location date) {
        this.rates = rates;
        this.date = date;
        this.ss = "hotel-provider";
        this.ts = Instant.now();
    }

}

