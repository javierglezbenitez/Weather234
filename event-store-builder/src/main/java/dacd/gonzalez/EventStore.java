package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import dacd.gonzalez.model.Weather;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventStore implements EventReciver {
    private static String ss = "prediction-weather";


    @Override
    public void mkdir(List<Weather> events){
        if (events.isEmpty()) {
            System.err.println("Weather list is empty. Nothing to write.");
            return;
        }

        // Escribir cada objeto Weather en un archivo separado
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        context.serialize(src.getEpochSecond()))
                .create();

        for (Weather weather : events) {
            // Obtener la marca de tiempo específica del objeto Weather
            Instant ts = weather.getTs();
            String tsFormatted = formatInstant(ts);

            String directoryPath = "eventstore\\prediction.Weather\\" + weather.getSs() + "\\" + tsFormatted;
            String filePath = directoryPath + "\\event-store.events";

            // Crear el directorio si no existe
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Directory created: " + directory.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directory: " + directory.getAbsolutePath());
                    return;
                }
            }

            // Escribir el objeto Weather en el archivo específico
            String json = gson.toJson(weather);
            writing(json, filePath);
        }
    }

    private void writing(String json, String filePath) {
        // Escribir el contenido en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(json);
            writer.newLine();
            System.out.println("Weather data written to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to write weather data to: " + filePath);
            e.printStackTrace();
        }
    }

    private String formatInstant(Instant instant) {
        // Formatear la marca de tiempo para usarla en el nombre del directorio
        return DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}