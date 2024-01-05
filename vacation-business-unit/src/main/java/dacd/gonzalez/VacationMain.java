package dacd.gonzalez;

import javax.jms.JMSException;

public class VacationMain {
    public static void main(String[] args) throws JMSException {

        Receiver receiver = new BrokerReceiver(args[0]);
        WeatherStorer weatherStorer = new WeatherDatamart();
        HotelStorer hotelStorer = new HotelDatamart();
        receiver.receiveMessage(weatherStorer, hotelStorer);


    }
}
