package fr.ubs.sporttrack.exception;

/**
 * Exception thrown when an {@link fr.ubs.sporttrack.model.Activity} already exist
 *
 * @author Raphael AIME
 */
public class ActivityAlreadyExistException extends Exception {
    public ActivityAlreadyExistException() {
        super("Activity already exist");
    }
}
