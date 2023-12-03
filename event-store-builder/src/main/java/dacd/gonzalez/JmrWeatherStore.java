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
    public List<Weather> receiveBrokerMessage() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(subscriberName);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);

            TopicSubscriber durableSubscriber = session.createDurableSubscriber((Topic) destination, "DurableSubscriber");

            List<Weather> receivedWeatherList = new ArrayList<>();

            durableSubscriber.setMessageListener(message -> {
                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    try {
                        String json = (String) objectMessage.getObject();
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (jsonElement, type, jsonDeserializationContext) ->
                                        Instant.ofEpochSecond(jsonElement.getAsLong()))
                                .create();

                        Weather weather = gson.fromJson(json, Weather.class);
                        receivedWeatherList.add(weather);
                        System.out.println(json);

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Wait until the desired number of messages is received or some timeout
            int receivedCount = 0;
            while (receivedCount < 40) {
                Thread.sleep(100);

                // Check if a message has been received
                if (!receivedWeatherList.isEmpty()) {
                    receivedCount++;
                    receivedWeatherList.clear(); // Clear the list for the next batch of messages
                }
            }

            connection.close();
            return receivedWeatherList;
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

