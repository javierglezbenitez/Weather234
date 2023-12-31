package dacd.gonzalez.model;

public class Location {
    private final String name;
    private final String hotelKey;

    private final String checkIn;
    private final String checkOut;
    private final String location;


    public Location(String checkIn, String checkOut, String hotelKey, String name,String location){
        this.hotelKey = hotelKey;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.name = name;
        this.location = location;
    }

    public String getHotelKey() {
        return hotelKey;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}


