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
    public void receiveMessage(Storer storer) {
        try {
            Topic destination = session.createTopic(HotelTopicName);

            MessageConsumer durableSubscriber = session.createDurableSubscriber(destination, client + HotelTopicName);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        storer.storeHotel(((TextMessage) message).getText());
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
                        storer.storeWeather(((TextMessage) message).getText());
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