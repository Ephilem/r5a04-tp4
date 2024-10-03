package fr.ubs.sporttrack.exception;

/**
 * Exception thrown when an {@link fr.ubs.sporttrack.model.User} is not found
 *
 * @author Raphael AIME
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String details) {
        super("User not found : " + details);
    }
}
