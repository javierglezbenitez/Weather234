package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import dacd.gonzalez.model.Hotel;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.time.Instant;
public class JmsHotelStore implements HotelStore  {
        private static String url;
        private static String topicName;
        public JmsHotelStore(String url, String topicName) {
            this.url = url;
            this.topicName = topicName;
        }
        @Override
        public void send(Hotel hotel) {
            try {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createTopic(topicName);

                MessageProducer producer = session.createProducer(destination);

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                                context.serialize(src.toString()))
                        .create();

                String json = gson.toJson(hotel);

                if (json != null && !json.equals("null")) {
                    TextMessage textMessage = session.createTextMessage(json);
                    producer.send(textMessage);
                    System.out.println("Hotel: " + json);
                } else {
                    System.out.println("Not object sent");
                }
                connection.close();
            } catch (JMSException e) {
                throw new RuntimeException(e);

            }
        }
    }

