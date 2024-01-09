package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileStateEventBuilder implements Listener {
    private   String directory;

    public FileStateEventBuilder(String directory) {
        this.directory = directory;
    }

    @Override
    public void write(String event, String topicName) throws JsonProcessingException {
        JsonObject jsonObject = new Gson().fromJson(event, JsonObject.class);
        String ss = jsonObject.getAsJsonPrimitive("ss").getAsString();
        String ts = jsonObject.getAsJsonPrimitive("ts").getAsString();

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.parse(ts), ZoneId.systemDefault());
        String date = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String path =   directory + File.separator + "datalakes" + File.separator + "eventstore" +   File.separator + topicName + File.separator + ss;
        File topicDirectory = new File(path);

        if (topicDirectory.mkdirs()) {
            System.out.println("Directory created: " + topicDirectory.getAbsolutePath());
        }

        String file = path + File.separator + date + ".events";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.write(event);
            bufferedWriter.newLine();
            System.out.println("File written to the directory: " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}