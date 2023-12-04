package dacd.gonzalez;

import dacd.gonzalez.model.Weather;

import java.util.List;

public interface EventStore {
    List<Weather> receive();
}
