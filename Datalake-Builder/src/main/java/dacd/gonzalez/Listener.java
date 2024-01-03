package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface Listener {
    void write(String event, String topicName) throws JsonProcessingException;
}