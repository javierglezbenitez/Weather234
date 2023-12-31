package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class AMQTopicSubscriber implements Subscriber {
    private final String url;
    private final Connection connection;
    private static String client = "prediction-provider";
    private final Session session;

    public AMQTopicSubscriber(String url) throws JMSException {
        this.url = url;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.setClientID(client);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Override
    public void receive(Listener listener, String topicName ){
        try {
            Destination destination = session.createTopic(topicName);

            MessageConsumer durableSubscriber = session.createDurableSubscriber((Topic) destination, "prediction-provider"+ topicName);

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String text = ((TextMessage) message).getText();
                        System.out.println("Received message: " + text);
                        listener.write(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Thread.sleep(Long.MAX_VALUE);
            connection.close();
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException("Error to receive the message", e);
        }
    }
}