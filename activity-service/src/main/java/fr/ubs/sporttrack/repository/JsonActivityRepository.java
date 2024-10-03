package fr.ubs.sporttrack.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ubs.sporttrack.exception.ActivityAlreadyExistException;
import fr.ubs.sporttrack.exception.ActivityNotFoundException;
import fr.ubs.sporttrack.model.Activity;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ActivityRepository} using a JSON file
 *
 * @see ActivityRepository
 * @author Raphael AIME
 */
@Repository
public class JsonActivityRepository implements ActivityRepository {

    private static final Logger logger = LoggerFactory.getLogger(JsonActivityRepository.class);

    private final AtomicInteger nextId = new AtomicInteger(0);
    private final ObjectMapper objectMapper;

    @Value("${activity-data.json.path}")
    private String jsonPath;
    private File jsonFile;

    public JsonActivityRepository() throws IOException {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Initialize the JSON file at the start of the application
     */
    @PostConstruct
    public void init() {
        // If path is not set or invalid, crash
        if (jsonPath == null || jsonPath.isBlank()) {
            throw new RuntimeException(String.format("Invalid path for JSON file: %s", jsonPath));
        }

        this.jsonFile = new File(jsonPath);

        // If the file does not exist, create it
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
                objectMapper.writeValue(jsonFile, new ArrayList<Activity>());
            } catch (IOException e) {
                logger.error("Error while creating JSON file", e);
                throw new RuntimeException("Error while creating JSON file");
            }
        }
    }

    @Override
    public List<Activity> findAll() {
        try {
            return objectMapper.readValue(jsonFile, new TypeReference<List<Activity>>() {});
        } catch (IOException e) {
            logger.error("Error while reading activities", e);
            return new ArrayList<>();
        }
    }

    @Override
    public synchronized Iterable<Activity> findAll(Sort sort) {
        List<Activity> activities = findAll();
        activities.sort(getComparator(sort));
        return activities;
    }

    @Override
    public synchronized Page<Activity> findAll(Pageable pageable) {
        List<Activity> activities;

        // Sorting
        if (pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            activities = (List<Activity>) findAll(sort);
        } else {
            activities = findAll();
        }

        // Pagination
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Activity> pagedList;

        if (activities.size() < startItem) {
            pagedList = new ArrayList<>();
        } else {
            int toIndex = Math.min(startItem + pageSize, activities.size());
            pagedList = activities.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedList, pageable, activities.size());
    }

    @Override
    public Page<Activity> findByDescriptionContainingIgnoreCase(String description, Pageable pageable) {
        List<Activity> activities = findAll();

        List<Activity> filteredActivities = activities.stream()
                .filter(activity -> activity.getDescription() != null && activity.getDescription().toLowerCase().contains(description.toLowerCase()))
                .collect(Collectors.toList());

        // Sorting
        if (pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            filteredActivities.sort(getComparator(sort));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredActivities.size());

        List<Activity> paginatedActivities = filteredActivities.subList(start, end);

        return new PageImpl<>(paginatedActivities, pageable, filteredActivities.size());
    }

    @Override
    public Optional<Activity> findById(int id) {
        List<Activity> activities = findAll();
        return activities.stream().filter(activity -> activity.getId() == id).findFirst();
    }

    @Override
    public boolean isExist(int id) {
        List<Activity> activities = findAll();
        return activities.stream().anyMatch(activity -> activity.getId() == id);
    }

    @Override
    public Activity save(Activity activity) throws ActivityAlreadyExistException {
        // Test if the id is valid
        if (isExist(activity.getId())) {
            throw new ActivityAlreadyExistException();
        }

        // Test if there is an id
        if (activity.getId() == 0) {
            activity.setId(getNextId());
        }

        // add into the json
        List<Activity> activities = findAll();
        activities.add(activity);

        try {
            objectMapper.writeValue(jsonFile, activities);
        } catch (IOException e) {
            logger.error("Error while saving activity", e);
            throw new RuntimeException("Error while saving activity");
        }

        return activity;
    }

    @Override
    public void deleteById(int id) throws ActivityNotFoundException {
        List<Activity> activities = findAll();
        int before = activities.size();
        activities.removeIf(activity -> activity.getId() == id);

        if (before == activities.size()) {
            throw new ActivityNotFoundException(id);
        }

        try {
            objectMapper.writeValue(jsonFile, activities);
        } catch (IOException e) {
            logger.error("Error while deleting activity", e);
            throw new RuntimeException("Error while deleting activity");
        }
    }

    @Override
    public Activity update(Activity activity) throws ActivityNotFoundException {
        List<Activity> activities = findAll();

        boolean found = false;
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getId() == activity.getId()) {
                activities.set(i, activity);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ActivityNotFoundException(activity.getId());
        }

        try {
            objectMapper.writeValue(jsonFile, activities);
        } catch (IOException e) {
            logger.error("Error while updating activity", e);
            throw new RuntimeException("Error while updating activity");
        }

        return activity;
    }

    @Override
    public int count() {
        return findAll().size();
    }




    private int getNextId() {
        int id = nextId.get();

        if (id == 0) {
            List<Activity> activities = findAll();
            nextId.set(activities.stream().map(Activity::getId)
                    .max(Integer::compareTo)
                    .orElse(0) + 1);
            id = nextId.get();
        } else {
            nextId.incrementAndGet();
        }

        return id;
    }

    private Comparator<Activity> getComparator(Sort sort) {
        if (!sort.isSorted()) {
            return Comparator.comparingInt(Activity::getId); // Default comparator if no sort is provided
        }

        Comparator<Activity> comparator = null;

        for (Sort.Order order : sort) {
            Comparator<Activity> orderComparator = switch (order.getProperty()) {
                case "id" -> Comparator.comparingInt(Activity::getId);
                case "date" -> Comparator.comparingLong(activity -> activity.getDate().getTime());
                case "description" -> Comparator.comparing(Activity::getDescription);
                case "distance" -> Comparator.comparingInt(Activity::getDistance);
                case "freqMin" -> Comparator.comparingInt(Activity::getFreqMin);
                case "freqMax" -> Comparator.comparingInt(Activity::getFreqMax);
                default -> Comparator.comparingInt(Activity::getId); // Default to sorting by id if the property is unknown
            };

            if (order.isDescending()) {
                orderComparator = orderComparator.reversed();
            }

            if (comparator == null) {
                comparator = orderComparator;
            } else {
                comparator = comparator.thenComparing(orderComparator);
            }
        }

        return comparator;
    }
}
