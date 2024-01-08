package dacd.gonzalez.control;

import dacd.gonzalez.model.Hotel;
import dacd.gonzalez.model.Location;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelController {
    private HotelProvider hotelProvider;
    private HotelStore hotelStore;
    public HotelController(HotelProvider hotelProvider, HotelStore hotelStore) {
        this.hotelProvider = hotelProvider;
        this.hotelStore = hotelStore;
    }

    public void execute() {
        LocalDate localDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(17, 0))) {
            localDate = localDate.plusDays(1);
        }

        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            String checkIn = localDate.format(DateTimeFormatter.ISO_DATE);
            String checkOut = localDate.plusDays(1).format(DateTimeFormatter.ISO_DATE);

            Location dubay = new Location(checkIn, checkOut, "g295424-d20326652", "Centara Mirage Beach Resort Dubai", "Dubai");
            Location spain = new Location(checkIn, checkOut, "g187497-d24119358", "Ramblas Hotel", "Madrid");
            Location thailand = new Location(checkIn, checkOut, "g293916-d1509981", "Marriott Executive Apartments", "Bang Kho Laem");
            Location paris = new Location(checkIn, checkOut, "g187147-d2041918", "Mandarin Oriental Paris", "Paris");
            Location newYorkCity = new Location(checkIn, checkOut, "g60763-d8501479", "Mint House at 70 Pine", "new_york");
            Location amsterdam = new Location(checkIn, checkOut, "g188590-d5555141", "Waldorf Astoria Amsterdam", "Amsterdam");
            Location nairobi = new Location(checkIn, checkOut, "g294207-d4091780", "Villa Rosa Kempinski", "Nairobi");
            Location milan = new Location(checkIn, checkOut, "g187849-d237325", "Lancaster Hotel", "Milan");

            locations.add(dubay);
            locations.add(spain);
            locations.add(thailand);
            locations.add(paris);
            locations.add(newYorkCity);
            locations.add(amsterdam);
            locations.add(nairobi);
            locations.add(milan);

            localDate = localDate.plusDays(1);

        }
        ArrayList<Hotel> hotels = new ArrayList<>();
        callWeatherGet(locations, hotels);
        callStored(locations);
    }
    public ArrayList<Hotel> callWeatherGet( List<Location> locations, ArrayList<Hotel> hotels) {
        for (Location iteredLocation : locations) {
            Hotel hotel = hotelProvider.getHotel(iteredLocation);
            hotels.add(hotel);
        }
        return hotels;
    }
    public  void callStored( List<Location> locations) {
        for (Location location : locations) {
            hotelStore.send(hotelProvider.getHotel(location));
        }
    }
}
