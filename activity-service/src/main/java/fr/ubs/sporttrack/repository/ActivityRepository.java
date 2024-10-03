package fr.ubs.sporttrack.repository;

import fr.ubs.sporttrack.exception.ActivityAlreadyExistException;
import fr.ubs.sporttrack.exception.ActivityNotFoundException;
import fr.ubs.sporttrack.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Activity} entities.
 * Provides methods for pagination, sorting, and searching.
 *
 * @author Raphael AIME
 */
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Integer> {

    /**
     * Retrieves all activities.
     *
     * @return the list of all activities
     */
    List<Activity> findAll();

    /**
     * Finds activities by description containing a search query, ignoring case, with pagination and sorting.
     *
     * @param description the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities matching the search query
     */
    Page<Activity> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    /**
     * Finds an activity by its ID.
     *
     * @param id the ID of the activity to find
     * @return an {@code Optional} containing the found activity, or empty if no activity is found
     */
    Optional<Activity> findById(int id);

    /**
     * Checks if an activity exists by its ID.
     *
     * @param id the ID of the activity to check
     * @return {@code true} if the activity exists, {@code false} otherwise
     */
    boolean isExist(int id);

    /**
     * Saves a new activity.
     * If the ID is already used, throws an {@link ActivityAlreadyExistException}.
     * If the ID is not set (inferior or equal to 0), sets it to the next available ID.
     *
     * @param activity the activity to save
     * @return the saved activity
     * @throws ActivityAlreadyExistException if an activity with the same ID already exists
     */
    Activity save(Activity activity) throws ActivityAlreadyExistException;

    /**
     * Deletes an activity by its ID.
     *
     * @param id the ID of the activity to delete
     * @throws ActivityNotFoundException if the activity with the given ID is not found
     */
    void deleteById(int id) throws ActivityNotFoundException;

    /**
     * Updates an existing activity.
     *
     * @param activity the activity to update
     * @return the updated activity
     * @throws ActivityNotFoundException if the activity with the given ID is not found
     */
    Activity update(Activity activity) throws ActivityNotFoundException;

    /**
     * Counts the total number of activities.
     *
     * @return the number of activities
     */
    int count();
}
