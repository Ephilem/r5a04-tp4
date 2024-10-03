package fr.ubs.sporttrack.repository;

import fr.ubs.sporttrack.model.Activity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * Repository interface for managing {@link Activity} entities.
 *
 * @author Raphael AIME
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /**
     * Finds activities by description containing a search query, ignoring case, with pagination and sorting.
     *
     * @param description the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities matching the search query
     */
    @NonNull
    Page<Activity> findByDescriptionContainingIgnoreCase(@NonNull String description, @NonNull Pageable pageable);

    /**
     * Finds activities by date.
     *
     * @param date the date to search for
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities on the given date
     */
    @NonNull
    Page<Activity> findByDate(@NonNull Date date, @NonNull Pageable pageable);

    /**
     * Finds activities within a date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities within the given date range
     */
    @NonNull
    Page<Activity> findByDateBetween(@NonNull Date startDate, @NonNull Date endDate, @NonNull Pageable pageable);

    /**
     * Finds activities by distance greater than or equal to the given value.
     *
     * @param distance the minimum distance
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities with distance greater than or equal to the given value
     */
    @NonNull
    Page<Activity> findByDistanceGreaterThanEqual(int distance, @NonNull Pageable pageable);

    /**
     * Finds activities by maximum heart rate greater than the given value.
     *
     * @param maxHeartRate the minimum value for maximum heart rate
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities with maximum heart rate greater than the given value
     */
    @NonNull
    Page<Activity> findByFreqMaxGreaterThan(int maxHeartRate, @NonNull Pageable pageable);

    /**
     * Checks if an activity exists by its description.
     *
     * @param description the description to check
     * @return {@code true} if an activity with the given description exists, {@code false} otherwise
     */
    boolean existsByDescription(@NonNull String description);

    /**
     * Finds an activity by its ID, including its associated ActivityData.
     *
     * @param id the ID of the activity to retrieve
     * @return an {@link Optional} containing the activity with its ActivityData, or empty if not found
     */
    @EntityGraph(attributePaths = "points")
    @Query("SELECT a FROM Activity a WHERE a.id = :id")
    Optional<Activity> findByIdWithActivityData(@NonNull Long id);
}
