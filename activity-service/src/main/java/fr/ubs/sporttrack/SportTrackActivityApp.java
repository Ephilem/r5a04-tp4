package fr.ubs.sporttrack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Starting point of the activity microservice
 * We desactivate the DataSourceAutoConfiguration because we don't need a database for this service (we use a JSON file)
 *
 * @author Raphael AIME
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SportTrackActivityApp {

    public static void main(String[] args){
        SpringApplication.run(SportTrackActivityApp.class, args);
    }

}
