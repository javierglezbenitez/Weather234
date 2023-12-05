package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface Listener {
    void consume(String message) throws JsonProcessingException;
}