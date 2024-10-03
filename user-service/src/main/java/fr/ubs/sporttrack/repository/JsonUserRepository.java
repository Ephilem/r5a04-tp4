package fr.ubs.sporttrack.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import fr.ubs.sporttrack.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserRepository} using a JSON file
 *
 * @see UserRepository
 * @author Raphael AIME
 */
@Repository
public class JsonUserRepository implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(JsonUserRepository.class);

    private final ObjectMapper objectMapper;
    private final AtomicInteger nextId = new AtomicInteger(0);

    @Value("${user-data.json.path}")
    private String jsonPath;
    private File jsonFile;

    public JsonUserRepository() throws IOException {
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
                objectMapper.writeValue(jsonFile, new ArrayList<User>());
            } catch (IOException e) {
                logger.error("Error while creating JSON file", e);
                throw new RuntimeException("Error while creating JSON file");
            }
        }

        initializeNextId();
    }

    private void initializeNextId() {
        List<User> users = findAll();
        int maxId = users.stream().mapToInt(User::getId).max().orElse(0);
        nextId.set(maxId + 1);
    }

    public String getJsonPath() {
        return jsonPath;
    }

    @Override
    public List<User> findAll() {
        try {
            return objectMapper.readValue(jsonFile, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            logger.error("Error while reading json file", e);
            return new ArrayList<>();
        }
    }

    @Override
    public synchronized Iterable<User> findAll(Sort sort) {
        List<User> activities = findAll();
        activities.sort(getComparator(sort));
        return activities;
    }

    @Override
    public synchronized Page<User> findAll(Pageable pageable) {
        List<User> activities;

        // Sorting
        if (pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            activities = (List<User>) findAll(sort);
        } else {
            activities = findAll();
        }

        // Pagination
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> pagedList;

        if (activities.size() < startItem) {
            pagedList = new ArrayList<>();
        } else {
            int toIndex = Math.min(startItem + pageSize, activities.size());
            pagedList = activities.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedList, pageable, activities.size());
    }

    @Override
    public Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable) {
        List<User> activities = findAll();

        List<User> filteredActivities = activities.stream()
                .filter(user -> user.getUsername() != null && user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());

        // Sorting
        if (pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            filteredActivities.sort(getComparator(sort));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredActivities.size());

        List<User> paginatedActivities = filteredActivities.subList(start, end);

        return new PageImpl<>(paginatedActivities, pageable, filteredActivities.size());
    }

    @Override
    public Optional<User> findById(int id) {
        List<User> users = findAll();
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public boolean isExist(int id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = findAll();
        return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = findAll();
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public User save(User user) throws UserAlreadyExistException {
        if (findByUsername(user.getUsername()).isPresent() || (user.getId() > 0 && isExist(user.getId()))) {
            throw new UserAlreadyExistException();
        }

        if (user.getId() <= 0) {
            user.setId(nextId.getAndIncrement());
        }

        List<User> users = findAll();
        users.add(user);

        try {
            objectMapper.writeValue(jsonFile, users);
        } catch (IOException e) {
            logger.error("Error while saving user", e);
            throw new RuntimeException("Error while saving user");
        }

        return user;
    }

    @Override
    public void deleteById(int id) throws UserNotFoundException {
        List<User> users = findAll();
        boolean removed = users.removeIf(user -> user.getId() == id);

        if (!removed) {
            throw new UserNotFoundException("Id " + id + " not found");
        }

        try {
            objectMapper.writeValue(jsonFile, users);
        } catch (IOException e) {
            logger.error("Error while deleting user", e);
            throw new RuntimeException("Error while deleting user");
        }
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        List<User> users = findAll();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new UserNotFoundException("Id " + user.getId() + " not found");
        }

        try {
            objectMapper.writeValue(jsonFile, users);
        } catch (IOException e) {
            logger.error("Error while updating user", e);
            throw new RuntimeException("Error while updating user");
        }

        return user;
    }

    @Override
    public int count() {
        return findAll().size();
    }



    private Comparator<User> getComparator(Sort sort) {
        if (!sort.isSorted()) {
            return Comparator.comparingInt(User::getId); // Default comparator if no sort is provided
        }

        Comparator<User> comparator = null;

        for (Sort.Order order : sort) {
            Comparator<User> orderComparator = switch (order.getProperty()) {
                case "id" -> Comparator.comparingInt(User::getId);
                case "username" -> Comparator.comparing(User::getUsername);
                case "email" -> Comparator.comparing(User::getEmail);
                default -> Comparator.comparingInt(User::getId);
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
