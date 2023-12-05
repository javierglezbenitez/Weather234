package dacd.gonzalez;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface Listener {
    void write(String message) throws JsonProcessingException;
}