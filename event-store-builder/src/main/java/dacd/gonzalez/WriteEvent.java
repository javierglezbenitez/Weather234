package dacd.gonzalez;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import dacd.gonzalez.model.Weather;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class WriteEvent {

    public static void mkdir(List<Weather> events){
        if (events.isEmpty()) {
            System.err.println("Weather list is empty.");
            return;
        }
        Weather firstWeather = events.get(0);
        String ts = firstWeather.getTs().toString().replace(":", "-");
        String directoryPath = "eventstore" + File.separator + "prediction.Weather" + File.separator + firstWeather.getSs() ;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                return;
            }
        }

        String filePath = directoryPath + File.separator + ts + ".events";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        context.serialize(src.getEpochSecond()))
                .create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Weather weather : events) {
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