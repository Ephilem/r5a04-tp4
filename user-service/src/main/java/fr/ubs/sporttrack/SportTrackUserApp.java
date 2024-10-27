package fr.ubs.sporttrack;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Starting point of the activity microservice
 *
 * @author Raphael AIME
 */
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
public class SportTrackUserApp {

    public static void main(String[] args){
        SpringApplication.run(SportTrackUserApp.class, args);
    }

}
