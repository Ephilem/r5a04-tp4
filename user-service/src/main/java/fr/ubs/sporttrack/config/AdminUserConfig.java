package fr.ubs.sporttrack.config;

import fr.ubs.sporttrack.model.User;
import fr.ubs.sporttrack.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing an admin user at application startup.
 * This class uses properties defined in the application configuration to create
 * an admin user if one does not already exist and auto-creation is enabled.
 *
 * @author Raphael AIME
 */
@Configuration
public class AdminUserConfig {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserConfig.class);

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.auto-create:false}")
    private boolean autoCreate;


    /**
     * Creates a CommandLineRunner bean to initialize the admin user.
     * This method checks if auto-creation is enabled and if an admin user
     * with the configured username does not already exist. If these conditions
     * are met, it creates a new admin user using the provided UserService.
     *
     * @param userService the UserService used to create and check for existing users
     * @return CommandLineRunner that initializes the admin user
     */
    @Bean
    public CommandLineRunner initializeAdminUser(UserService userService) {
        return args -> {
            if (autoCreate && userService.findUsersByUsername(adminUsername).isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername(adminUsername);
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(adminPassword);
                try {
                    userService.saveUser(adminUser);
                    logger.info("Admin user created successfully");
                } catch (Exception e) {
                    logger.error("Failed to create admin user", e);
                }
            }
        };
    }
}
