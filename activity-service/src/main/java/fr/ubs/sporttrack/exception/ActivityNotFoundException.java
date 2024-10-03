package fr.ubs.sporttrack.exception;

/**
 * Exception thrown when an {@link fr.ubs.sporttrack.model.Activity} is not found
 *
 * @author Raphael AIME
 */
public class ActivityNotFoundException extends Exception {
    private final long id;

    /**
     * Constructor
     * @param id the id of the not found activity
     */
    public ActivityNotFoundException(long id) {
        super("Activity not found with id: " + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
