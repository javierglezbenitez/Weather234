package dacd.gonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Main {
    public static void main(String[] args) {


            // Crea un ScheduledExecutorService con un hilo para ejecutar tareas programadas
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            // Programa la tarea para ejecutarse cada seis horas
            scheduler.scheduleAtFixedRate(() -> {
                execute();
            }, 0, 6, TimeUnit.HOURS);
        }
        private static void execute() {
            // Aquí colocas la lógica que deseas ejecutar cada seis horas
            System.out.println("Ejecutando la tarea cada seis horas");

        }
    }