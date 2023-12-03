package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JmrWeatherStore implements WeatherReciever {


    private static String url;
    private static String topicName;
    private static String subscriberName;


    public JmrWeatherStore(String url, String topicName, String suscriberName) {
        this.url = url;
        this.topicName = topicName;
        this.subscriberName = suscriberName;

    }

    @Override
    public List<String>  receiveBrokerMessage() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(subscriberName);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);

            TopicSubscriber durableSubscriber = session.createDurableSubscriber((Topic) destination, "DurableSubscriber");

            List<String> receivedWeatherList = new ArrayList<>();

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    try {
                        String json = (String) objectMessage.getObject();
                        // Aquí puedes realizar operaciones adicionales si es necesario
                        receivedWeatherList.add(json);

                        System.out.println("Received Weather: " + json);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Esperar hasta que se reciba un mensaje
            while (receivedWeatherList.isEmpty()) {
                Thread.sleep(100);
            }

            connection.close();
            return receivedWeatherList;
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}


