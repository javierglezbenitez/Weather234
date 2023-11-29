package dacd.gonzalez.control;

import javax.jms.*;

import com.google.gson.Gson;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.time.Instant;
import java.util.Map;

  public class JmsWeatherStore implements WeatherStore {

      private final String url;
      private final String topic;

      public JmsWeatherStore(String url, String topic) {

          this.url = url;
          this.topic = topic;
      }

      @Override
      public void save(Weather weather) {
          try{
          ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
          Connection connection = connectionFactory.createConnection();
          connection.start();

          Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          Destination destination = session.createQueue(topic);

          MessageProducer producer = session.createProducer(destination);

          // Creamos un objeto Serializable (puedes reemplazar 'YourObject' con el tipo de objeto que estás enviando)
              Gson gson = new Gson();
              String json = gson.toJson(weather);


          ObjectMessage objectMessage = session.createObjectMessage(json);

          System.out.println(json);
          // Enviamos el mensaje
          producer.send(objectMessage);

          System.out.println("JCG printing@@ '" + json + "'");
          connection.close();
      } catch (JMSException e) {
              throw new RuntimeException(e);
          }
      }
  }
