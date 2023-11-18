package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import java.time.Instant;

public interface WeatherProvider {
     Weather WeatherGet(Location location, Instant instant);

    }







