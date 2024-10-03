package fr.ubs.sporttrack.controller.dto;

import fr.ubs.sporttrack.model.User;

/**
 * A Data Transfer Object (DTO) for {@link User} entities.
 * Used to transfer user data between the client and the server
 * without exposing the password.
 *
 * @author Raphael AIME
 */
public class UserDTO {
    private int id;
    private String username;
    private String email;

    /**
     * Construct from a {@link User} entity.
     *
     * @param user the user entity
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    /**
     * Construct from the user's id, username and email.
     *
     * @param id the user's id
     * @param username the user's username
     * @param email the user's email
     */
    public UserDTO(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters

    /**
     * Get the user's id.
     *
     * @return the user's id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the user's id.
     *
     * @param id the user's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user's username.
     *
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the user's email.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email.
     *
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}