package fr.ubs.sporttrack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Starting point of the activity microservice
 * We desactivate the DataSourceAutoConfiguration because we don't need a database for this service (we use a JSON file)
 *
 * @author Raphael AIME
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SportTrackActivityApp {

    public static void main(String[] args){
        SpringApplication.run(SportTrackActivityApp.class, args);
    }

}
