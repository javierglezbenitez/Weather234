package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import java.time.Instant;

public  interface WeatherStore {
    void save(Location location, Instant instant);



}
