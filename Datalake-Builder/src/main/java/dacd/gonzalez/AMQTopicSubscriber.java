package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class AMQTopicSubscriber implements Subscriber {
    private final String url;
    private final Connection connection;
    private static String client = "prediction";
    private final Session session;
    private final String HotelTopicName = "prediction.Hotel";
    private final String WeatherTopicName = "prediction.Weather";

    public AMQTopicSubscriber(String url) throws JMSException {
        this.url = url;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.setClientID(client);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void receive(Listener listener){
        try {
            Topic destination = session.createTopic(HotelTopicName);

            MessageConsumer durableSubscriber = session.createDurableSubscriber(destination, client + HotelTopicName);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        listener.write(((TextMessage) message).getText(), HotelTopicName);
                    } catch (JMSException | JsonProcessingException e) {
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
                        listener.write(((TextMessage) message).getText(), WeatherTopicName);
                    } catch (JMSException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);

        }
    }
}