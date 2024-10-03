package fr.ubs.sporttrack.controller.dto;



/**
 * Data Transfer Object for login.
 * This DTO is used to transfer data from the client to the server when authenticating a user.
 *
 * @see fr.ubs.sporttrack.controller.AuthController
 * @author Raphael AIME
 */
public class AuthRequestDTO {

    private String username;
    private String password;

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
     * Get the password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
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
