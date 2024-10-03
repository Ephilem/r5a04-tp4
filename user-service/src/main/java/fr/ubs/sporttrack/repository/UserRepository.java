package fr.ubs.sporttrack.repository;

import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import fr.ubs.sporttrack.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * @author Raphael AIME
 */
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    /**
     * Retrieves all users.
     *
     * @return the list of all users
     */
    List<User> findAll();

    /**
     * Finds users by username containing a search query, ignoring case, with pagination and sorting.
     *
     * @param description the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of users matching the search query
     */
    Page<User> findByUsernameContainingIgnoreCase(String description, Pageable pageable);

    /**
     * Searches for a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user with the given username if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Searches for a user by their email.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user with the given email if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an {@link Optional} containing the user with the given ID if found, or empty otherwise
     */
    Optional<User> findById(int id);

    /**
     * Checks if a user exists by their ID.
     *
     * @param id the ID of the user to check
     * @return {@code true} if the user exists, {@code false} otherwise
     */
    boolean isExist(int id);

    /**
     * Saves a new user.
     * If the ID or username is already used, throws a {@link UserAlreadyExistException}.
     * If the ID is not set (inferior or equal to 0), sets it to the next available ID.
     *
     * @param user the user to save
     * @return the saved user
     * @throws UserAlreadyExistException if a user with the same ID already exists
     */
    User save(User user) throws UserAlreadyExistException;

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    void deleteById(int id) throws UserNotFoundException;

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return the updated user
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    User update(User user) throws UserNotFoundException;

    /**
     * Counts the total number of users.
     *
     * @return the number of users
     */
    int count();
}
