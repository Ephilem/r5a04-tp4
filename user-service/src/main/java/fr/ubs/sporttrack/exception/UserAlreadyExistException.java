package fr.ubs.sporttrack.exception;

/**
 * Exception thrown when a {@link fr.ubs.sporttrack.model.User} already exist
 *
 * @author Raphael AIME
 */
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exist");
    }
}
