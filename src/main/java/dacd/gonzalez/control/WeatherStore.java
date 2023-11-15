package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;

import java.time.Instant;

public interface WeatherStore {
    void save(Weather weather);

    void load(Location location, Instant instant);
}
