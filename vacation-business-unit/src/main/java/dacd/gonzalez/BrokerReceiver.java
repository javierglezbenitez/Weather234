package dacd.gonzalez;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class BrokerReceiver implements  Receiver {
    private final Connection connection;
    private static String client = "prediction-provider";
    private final Session session;
    private final String HotelTopicName = "prediction.Hotel";
    private final String WeatherTopicName = "prediction.Weather";
    private final String url;


    public BrokerReceiver(String url) throws JMSException {
        this.url = url;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.setClientID(client);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void receiveMessage(WeatherStorer weatherStorer, HotelStorer hotelStorer) {
        try {
            Topic destination = session.createTopic(HotelTopicName);

            MessageConsumer durableSubscriber = session.createDurableSubscriber(destination, client + HotelTopicName);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        hotelStorer.storeHotel(((TextMessage) message).getText(), HotelTopicName);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        try {
            Topic destination = session.createTopic(WeatherTopicName);

            MessageConsumer durableSubscriber = session.createDurableSubscriber(destination, client + WeatherTopicName);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        weatherStorer.storeWeather(((TextMessage) message).getText(), WeatherTopicName);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);

        }
    }
}
