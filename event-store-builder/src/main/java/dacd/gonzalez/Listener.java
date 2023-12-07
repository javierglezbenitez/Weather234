package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface Listener {
    void write(String event) throws JsonProcessingException;
}