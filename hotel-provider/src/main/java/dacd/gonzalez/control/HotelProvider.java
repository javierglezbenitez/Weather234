package dacd.gonzalez.control;

import dacd.gonzalez.model.Hotel;
import dacd.gonzalez.model.Location;

public interface HotelProvider {
    Hotel getHotel(Location location);
}
