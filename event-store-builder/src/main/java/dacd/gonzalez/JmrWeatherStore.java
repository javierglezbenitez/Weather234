package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.Instant;

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
    public void receiveBrokerMessage() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(subscriberName);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);

            TopicSubscriber durableSubscriber = session.createDurableSubscriber((Topic) destination, "DurableSubscriber");

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

                        // Ahora puedes hacer lo que quieras con el objeto Weather
                        System.out.println("Received Weather: " + json);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Mantén el hilo principal esperando para recibir mensajes
            Thread.sleep(Long.MAX_VALUE);

            connection.close();
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

