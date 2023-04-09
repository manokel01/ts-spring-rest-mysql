package tinysensormanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the TinySensor Spring Boot application.
 * This class is responsible for starting up the Spring Boot application and initializing the Spring context.
 * It is annotated with the @SpringBootApplication annotation, which enables Spring Boot auto-configuration
 * and component scanning.
 *
 * @author manokel01
 * @version 1.0.0
 */
@SpringBootApplication
public class TinysensorSpringbootApplication {

    /**
     * The main method of the application. It calls the SpringApplication.run method with the
     * TinysensorSpringbootApplication class and command-line arguments, which starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(TinysensorSpringbootApplication.class, args);
    }

}

