package dacd.gonzalez.control;

import dacd.gonzalez.model.Hotel;
import dacd.gonzalez.model.Location;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        List<Location> hotelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            String checkIn = localDate.format(DateTimeFormatter.ISO_DATE);
            String checkOut = localDate.plusDays(1).format(DateTimeFormatter.ISO_DATE);

            Location dubay = new Location(checkIn, checkOut, "g295424-d20326652", "Centara Mirage Beach Resort Dubai", "Dubai");
            Location dubay1 = new Location(checkIn, checkOut, "g295424-d1022759","Atlantis The Palm" ,"Dubai");
            Location spain = new Location(checkIn, checkOut, "g187497-d24119358", "Ramblas Hotel", "Madrid");
            Location spain1 = new Location(checkIn, checkOut, "g187443-d471980", "Gravina51 Hotel", "Madrid");
            Location thailand = new Location(checkIn, checkOut, "g293916-d1509981", "Marriott Executive Apartments", "Bang Kho Laem");
            Location thailand1 = new Location(checkIn, checkOut, "g293916-d20146210", "Carlton Hotel Bangkok Sukhumvit", "Bang Kho Laem");
            Location paris = new Location(checkIn, checkOut, "g187147-d2041918", "Mandarin Oriental Paris", "Paris");
            Location paris2 = new Location(checkIn, checkOut, "g187147-d230431", "Hotel Astoria", "Paris");
            Location newYorkCity = new Location(checkIn, checkOut, "g60763-d8501479", "Mint House at 70 Pine", "New York");
            Location newYorkCity1 = new Location(checkIn, checkOut, "g60763-d26865916", "Roomza Times Square at Pestana CR7", "New York");
            Location amsterdam = new Location(checkIn, checkOut, "g188590-d5555141", "Waldorf Astoria Amsterdam", "Amsterdam");
            Location amsterdam1 = new Location(checkIn, checkOut, "g188590-d232321", "Hotel Ambassade", "Amsterdam");
            Location nairobi = new Location(checkIn, checkOut, "g294207-d4091780", "Villa Rosa Kempinski", "Nairobi");
            Location nairobi1 = new Location(checkIn, checkOut, "g294207-d7323251", "Yaya Hotel and Apartments", "Nairobi");
            Location milan = new Location(checkIn, checkOut, "g187849-d237325", "Lancaster Hotel", "Milan");
            Location milan1 = new Location(checkIn, checkOut, "g187849-d194976", "Hotel Spadari Al Duomo", "Milan");

            hotelList.add(dubay);
            hotelList.add(dubay1);
            hotelList.add(spain);
            hotelList.add(spain1);
            hotelList.add(thailand);
            hotelList.add(thailand1);
            hotelList.add(paris);
            hotelList.add(paris2);
            hotelList.add(newYorkCity);
            hotelList.add(newYorkCity1);
            hotelList.add(amsterdam);
            hotelList.add(amsterdam1);
            hotelList.add(nairobi);
            hotelList.add(nairobi1);

            hotelList.add(milan);
            hotelList.add(milan1);

            localDate = localDate.plusDays(1);

        }
        ArrayList<Hotel> hotels = new ArrayList<>();
        callHotelGet(hotelList, hotels);
        send(hotelList);
    }
    public ArrayList<Hotel> callHotelGet( List<Location> locations, ArrayList<Hotel> hotels) {
        for (Location iteredLocation : locations) {
            Hotel hotel = hotelProvider.getHotel(iteredLocation);
            hotels.add(hotel);
        }
        return hotels;
    }
    public  void send( List<Location> locations) {
        for (Location location : locations) {
            hotelStore.send(hotelProvider.getHotel(location));
        }
    }
}
