package fr.ubs.sporttrack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Starting point of the activity microservice
 *
 * @author Raphael AIME
 */
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class SportTrackUserApp {

    public static void main(String[] args){
        SpringApplication.run(SportTrackUserApp.class, args);
    }

}
