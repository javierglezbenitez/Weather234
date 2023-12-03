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


    @Override
    public void mkdir(List<String> events){
        if (events.isEmpty()) {
            System.err.println("Events list is empty. Nothing to write.");
            return;
        }

        // Tomamos el primer evento para obtener información común

        String ts = Instant.now().toString().replace(":", "-");

        String directoryPath = "eventstore\\prediction.Weather\\" + "prediction-weather" + "\\" + ts;
        String filePath = directoryPath + "\\event-store.events";

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

        // Escribir toda la lista de eventos en el archivo
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        context.serialize(src.getEpochSecond()))
                .create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String event : events) {
                String json = gson.toJson(event);
                writer.write(json);
                writer.newLine();
            }
            System.out.println("Event data written to: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to write event data to: " + filePath);
            e.printStackTrace();
        }
    }
}
