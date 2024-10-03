package fr.ubs.sporttrack.controller.dto;

import fr.ubs.sporttrack.model.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

/**
 * Data Transfer Object for updating an {@link User}.
 * This DTO is used to transfer data from the client to the server when updating a new user.
 *
 * @author Raphael AIME
 */
public class UpdateUserDTO {


    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String password;

    // Getters and Setters

    /**
     * Get the username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the email
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password
     *
     * @return the password
     */
    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    /**
     * Set the password
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}