package dacd.gonzalez.Model;

public interface Command {
    void displayAverageWeatherData(String country, String checkInDate, String checkOutDate);
    void recomendarHoteles(String country);
}
