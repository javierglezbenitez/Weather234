package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import dacd.gonzalez.model.Weather;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
    public List<Weather> receiveBrokerMessage() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(subscriberName);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);

            TopicSubscriber durableSubscriber = session.createDurableSubscriber((Topic) destination, "DurableSubscriber");

            List<Weather> receivedWeatherList = Collections.synchronizedList(new ArrayList<>());

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

                        // Asegúrate de manejar la sincronización correctamente
                        synchronized (receivedWeatherList) {
                            receivedWeatherList.add(weather);
                        }

                        System.out.println("Received Weather: " + weather);
                        System.out.println(receivedWeatherList.size());
                        writeWeatherListToDirectory(receivedWeatherList, "tupadrebro");

                        // Puedes agregar aquí lógica adicional si es necesario
                    } catch (JMSException e) {
                        // Manejar la excepción de manera significativa para tu aplicación
                        throw new RuntimeException("Error while processing JMS message", e);
                    }
                }
            });

            // Esperar hasta que se alcance el tiempo de espera
            Thread.sleep(30000); // Espera máximo 30 segundos (ajusta según tus necesidades)

            connection.close();
            return receivedWeatherList;
        } catch (JMSException | InterruptedException e) {
            // Manejar la excepción de manera significativa para tu aplicación
            throw new RuntimeException("Error while receiving JMS message", e);
        }
    }
    public void writeWeatherListToDirectory(List<Weather> weatherList, String directoryPath) {
        if (weatherList.isEmpty()) {
            System.err.println("Weather list is empty. Nothing to write.");
            return;
        }

        // Obtener el primer objeto Weather para información común
        Weather firstWeather = weatherList.get(0);
        String ts = firstWeather.getTs().toString().replace(":", "-");

        // Crear el directorio si no existe
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                return;
            }
        }

        // Escribir todos los objetos Weather en el archivo
        String filePath = directoryPath + File.separator + "weather-data.events";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        context.serialize(src.getEpochSecond()))
                .create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Weather weather : weatherList) {
                String json = gson.toJson(weather);
                writer.write(json);
                writer.newLine();
            }
            System.out.println("Weather data written to: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to write weather data to: " + filePath);
            e.printStackTrace();
        }
    }

}

