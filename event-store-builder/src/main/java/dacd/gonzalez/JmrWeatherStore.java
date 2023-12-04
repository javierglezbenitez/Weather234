package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JmrWeatherStore implements EventStore {


    private static String url;
    private static String topic;
    private static String subscriberName;


    public JmrWeatherStore(String url, String topic, String suscriberName) {
        this.url = url;
        this.topic = topic;
        this.subscriberName = suscriberName;

    }


    @Override
    public List<Weather> receive() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(subscriberName);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);

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


                        System.out.println("Received Weather: " + weather);
                        System.out.println(receivedWeatherList.size());
                        WriteEvent.mkdir(receivedWeatherList);

                    } catch (JMSException e) {
                        throw new RuntimeException("Error while processing JMS message", e);
                    }
                }
            });

            Thread.sleep(30000);

            connection.close();
            return receivedWeatherList;
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException("JMS message not recieve", e);
        }
    }
}

