package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileStateEventBuilder implements Listener {
    private  String url;

    public FileStateEventBuilder(String url) {
        this.url = url;
    }
    @Override
    public void write(String message) throws JsonProcessingException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

        String ssValue = jsonObject.get("ss").getAsString();
        String tsValue = jsonObject.get("ts").getAsString();

        Instant instant = Instant.parse(tsValue);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = dateTime.format(formatter);

        String path = url + File.separator + ssValue;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Directory created");
        }

        String filePath = path + File.separator + date + ".events";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(message);
            writer.newLine();
            System.out.println("File written to the directory: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}