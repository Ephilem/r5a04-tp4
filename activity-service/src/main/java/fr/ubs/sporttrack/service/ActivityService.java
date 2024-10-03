package fr.ubs.sporttrack.service;

import fr.ubs.sporttrack.exception.ActivityAlreadyExistException;
import fr.ubs.sporttrack.exception.ActivityNotFoundException;
import fr.ubs.sporttrack.model.Activity;
import fr.ubs.sporttrack.model.ActivityData;
import fr.ubs.sporttrack.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link Activity} entities.
 * This service provides methods to perform CRUD operations on Activity entities.
 *
 * @author Raphael AIME
 */
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    /**
     * Constructs an {@code ActivityService} with the specified {@code ActivityRepository}.
     *
     * @param activityRepository the repository used for accessing activity data
     */
    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    /**
     * Retrieves all activities with pagination and sorting.
     *
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities
     */
    public Page<Activity> findAll(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }

    /**
     * Searches activities by description with pagination and sorting.
     *
     * @param search the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of activities matching the search query
     */
    public Page<Activity> searchActivities(String search, Pageable pageable) {
        return activityRepository.findByDescriptionContainingIgnoreCase(search, pageable);
    }

    /**
     * Retrieves an activity by its ID.
     *
     * @param id the ID of the activity to retrieve
     * @return an {@code Optional} containing the found activity, or empty if no activity found
     */
    public Optional<Activity> findById(long id) {
        return activityRepository.findByIdWithActivityData(id);
    }

    /**
     * Saves a new activity.
     *
     * @param activity the activity to save
     * @return the saved activity
     * @throws ActivityAlreadyExistException if an activity with the same ID already exists
     */
    public Activity save(Activity activity) throws ActivityAlreadyExistException {
        if (activity.getId() != null && activityRepository.existsById(activity.getId())) {
            throw new ActivityAlreadyExistException();
        }

        // Set the activity reference for each ActivityData
        if (activity.getPoints() != null) {
            for (ActivityData data : activity.getPoints()) {
                data.setActivity(activity);
            }
        }

        return activityRepository.save(activity);
    }

    /**
     * Deletes an activity by its ID.
     *
     * @param id the ID of the activity to delete
     * @throws ActivityNotFoundException if no activity with the specified ID is found
     */
    public void deleteById(long id) throws ActivityNotFoundException {
        if (!activityRepository.existsById(id)) {
            throw new ActivityNotFoundException(id);
        }
        activityRepository.deleteById(id);
    }

    /**
     * Updates an existing activity.
     *
     * @param activity the activity to update
     * @return the updated activity
     * @throws ActivityNotFoundException if no activity with the specified ID is found
     */
    public Activity update(Activity activity) throws ActivityNotFoundException {
        if (!activityRepository.existsById(activity.getId())) {
            throw new ActivityNotFoundException(activity.getId());
        }

        // Set the activity reference for each ActivityData
        if (activity.getPoints() != null) {
            for (ActivityData data : activity.getPoints()) {
                data.setActivity(activity);
            }
        }

        return activityRepository.saveAndFlush(activity);
    }

    /**
     * Retrieves the total count of activities.
     *
     * @return the total count of activities
     */
    public long count() {
        return activityRepository.count();
    }


}
