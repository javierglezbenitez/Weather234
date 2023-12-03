package dacd.gonzalez;

import dacd.gonzalez.model.Weather;

import java.util.List;

public interface WeatherReciever {
    List<String>  receiveBrokerMessage();
}
