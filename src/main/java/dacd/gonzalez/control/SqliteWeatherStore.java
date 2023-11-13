package dacd.gonzalez.control;

import dacd.gonzalez.model.Location;
import dacd.gonzalez.model.Weather;

import java.sql.*;
import java.time.Instant;


public class SqliteWeatherStore implements WeatherStore {


    @Override
    public void load(Location location, Instant instant) {
        WeatherProvider weatherProvider = new MapWeatherProvider();
        Weather weather = weatherProvider.WeatherGet(location, instant);
        if (weather != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/cgsos/Downloads/Bases de datos/Weather.db");
                String createTableSQL = "CREATE TABLE IF NOT EXISTS weather (" +
                        "name TEXT ," +
                        "clouds INTEGER," +
                        "wind REAL," +
                        "temperature REAL," +
                        "humidity INTEGER," +
                        "instant TEXT," +
                        "pop REAL" +
                        ")";

                Statement statement = connection.createStatement();
                statement.executeUpdate(createTableSQL);

                String insertWeatherSQL = "INSERT INTO weather (name, clouds, wind, temperature, humidity, instant, pop) VALUES (?, ?, ?, ?, ?, ?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertWeatherSQL);

                preparedStatement.setString(1, location.getName());
                preparedStatement.setInt(2, weather.getAll());
                preparedStatement.setDouble(3, weather.getSpeed());
                preparedStatement.setDouble(4, weather.getTemp());
                preparedStatement.setInt(5, weather.getHumidity());
                preparedStatement.setString(6, weather.getDt().toString());
                preparedStatement.setDouble(7, weather.getPop());
                preparedStatement.executeUpdate();

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

                }
            }else{
                    System.out.println("No weather data found for ");
                }

            }

        }
