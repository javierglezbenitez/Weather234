package dacd.gonzalez.Controller;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class BrokerReceiver implements Receiver {
    private final Connection connection;
    private final Session session;
    private final String HotelTopicName = "prediction.Hotel";
    private final String WeatherTopicName = "prediction.Weather";
    private final String url;


    public BrokerReceiver(String url) throws JMSException {
        this.url = url;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void receiveMessage(Storer storer) {
        try {
            Topic destination = session.createTopic(HotelTopicName);

            MessageConsumer durableSubscriber = session.createConsumer(destination);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        storer.HotelStore(((TextMessage) message).getText());
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

            MessageConsumer durableSubscriber = session.createConsumer(destination);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        storer.WeatherStore(((TextMessage) message).getText());
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
