package dacd.gonzalez.control;

import javax.jms.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.time.Instant;
import java.util.Map;

  public class JmsWeatherStore implements WeatherStore {

      private static String url;
      private static String topic;

      public JmsWeatherStore(String url, String topic) {

          this.url = url;
          this.topic = topic;
      }

      @Override
      public void save(Location location, Instant instant) {
          WeatherProvider weatherProvider = new MapWeatherProvider(MapWeatherProvider.getApiKey());
          Weather weather = weatherProvider.getWeather(location, instant);
          try{
          ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
          Connection connection = connectionFactory.createConnection();
          connection.start();

          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          Destination destination = session.createTopic(topic);

          MessageProducer producer = session.createProducer(destination);

              Gson gson = new GsonBuilder()
                      .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                              context.serialize(src.getEpochSecond()))
                      .create();

              String json = gson.toJson(weather);


          ObjectMessage objectMessage = session.createObjectMessage(json);

          producer.send(objectMessage);

          System.out.println("Tiempo de" + location.getName() + ":" + json );
          connection.close();
      } catch (JMSException e) {
              throw new RuntimeException(e);
          }
      }
  }
