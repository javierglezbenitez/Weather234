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

    public void execute(){
        LocalDate localDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (currentTime.isAfter(LocalTime.of(17, 0))) {
            localDate = localDate.plusDays(1);
        }

        String checkIn = localDate.format(DateTimeFormatter.ISO_DATE);
        String checkOut = localDate.plusDays(5).format(DateTimeFormatter.ISO_DATE);


        Location dubay1 = new Location(checkIn, checkOut, "g295424-d20326652", "Centara Mirage Beach Resort Dubai", "Dubay");
        Location dubay2 = new Location(checkIn, checkOut, "g295424-d3447941", "JW Marriott Marquis Dubai", "Dubay");
        Location dubay3 = new Location(checkIn, checkOut, "g295424-d7309237", "Taj Dubai", "Dubay");
        Location spain1 = new Location(checkIn, checkOut, "g187497-d24119358", "Ramblas Hotel", "Spain");
        Location spain2 = new Location(checkIn, checkOut, "g187497-d190616", "Majestic Hotel  Spa Barcelona", "Spain");
        Location spain3 = new Location(checkIn, checkOut, "g187514-d228529", "Palacio de los Duques Gran Melia", "Spain");
        Location thailand1 = new Location(checkIn, checkOut, "g293916-d1509981", "Marriott Executive Apartments", "Thailand");
        Location thailand2 = new Location(checkIn, checkOut, "g293916-d300434", "Amari Bangkok", "Thailand");
        Location thailand3 = new Location(checkIn, checkOut, "g293916-d1724201", "Siam Kempinski Hotel Bangkok", "Thailand");
        Location paris1 = new Location(checkIn, checkOut, "g187147-d2041918", "Mandarin Oriental Paris", "Paris");
        Location paris2 = new Location(checkIn, checkOut, "g187147-d188729", "Le Bristol Paris", "Paris");
        Location paris3 = new Location(checkIn, checkOut, "g187147-d617625", "Grand Hotel du Palais Royal", "Paris");
        Location newYorkCity1 = new Location(checkIn, checkOut, "g60763-d8501479", "Mint House at 70 Pine", "New York City");
        Location newYorkCity2 = new Location(checkIn, checkOut, "g60763-d675616", "The Plaza New York", "New York City");
        Location newYorkCity3 = new Location(checkIn, checkOut, "g60763-d1456416", "The Dominick Hotel", "New York City");
        Location amsterdam1 = new Location(checkIn, checkOut, "g188590-d5555141", "Waldorf Astoria Amsterdam", "Amsterdam");
        Location amsterdam2 = new Location(checkIn, checkOut, "g188590-d229182", "Anantara Grand Hotel Krasnapolsky", "Amsterdam");
        Location amsterdam3 = new Location(checkIn, checkOut, "g188590-d229151", "Hotel Estherea", "Amsterdam");
        Location nairobi1 = new Location(checkIn, checkOut, "g294207-d4091780", "Villa Rosa Kempinski", "Nairobi");
        Location nairobi2 = new Location(checkIn, checkOut, "g294207-d19744017", "The Social House Nairobi", "Nairobi");
        Location nairobi3 = new Location(checkIn, checkOut, "g294207-d1158294", "Tribe Hotel", "Nairobi");
        Location milan1 = new Location(checkIn, checkOut, "g187849-d237325", "Lancaster Hotel", "Milan");
        Location milan2 = new Location(checkIn, checkOut, "g187849-d2340336", "Armani Hotel", "Milan");
        Location milan3 = new Location(checkIn, checkOut, "g187849-d237312", "Gran Duca di York", "Milan");

        List<Location> locations = List.of(dubay1, dubay2, dubay3, spain1,spain2, spain3, thailand1, thailand2, thailand3
                ,paris1, paris2, paris3, newYorkCity1, newYorkCity2, newYorkCity3, amsterdam1, amsterdam2, amsterdam3
                ,nairobi1, nairobi2, nairobi3, milan1, milan2, milan3
        );
        ArrayList<Hotel> hotels= new ArrayList<>();

        callWeatherGet(locations, hotels);
        callStored(locations);


    }

    public ArrayList<Hotel> callWeatherGet( List<Location> locations, ArrayList<Hotel> hotels) {
        for (Location iteredLocation : locations) {
            Hotel hotel = hotelProvider.getHotel(iteredLocation);
            hotels.add(hotel);

        }
        System.out.println(hotels.size());
        return hotels;
    }

    public  void callStored( List<Location> locations) {
        for (Location location : locations) {
            hotelStore.send(hotelProvider.getHotel(location));

        }
    }

}
