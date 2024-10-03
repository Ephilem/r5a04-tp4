package fr.ubs.sporttrack.exception;

/**
 * Exception thrown when a {@link fr.ubs.sporttrack.model.User} already exist
 *
 * @author Raphael AIME
 */
public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("User already exist");
    }
}
