package fr.ubs.sporttrack.controller.dto;

import fr.ubs.sporttrack.service.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Data Transfer Object for login.
 * This DTO is used to respond to the client when authenticating a user.
 *
 * @see fr.ubs.sporttrack.controller.AuthController
 * @author Raphael AIME
 */
public class AuthResponseDTO {
    private final String token;
    private final UserDTO user;

    public AuthResponseDTO(String token, UserDTO userDetails) {
        this.token = token;
        this.user = userDetails;
    }

    /**
     * Get the JWT token
     *
     * @return the JWT token
     */
    public String getToken() {
        return token;
    }

    /**
     * Get the user details
     *
     * @return the user details
     */
    public UserDTO getUser() {
        return user;
    }
}
