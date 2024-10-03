package fr.ubs.sporttrack.repository;

import fr.ubs.sporttrack.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * @author Raphael AIME
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds users by username containing a search query, ignoring case, with pagination and sorting.
     *
     * @param username the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of users matching the search query
     */
    @NonNull
    Page<User> findByUsernameContainingIgnoreCase(@NonNull String username, @NonNull Pageable pageable);

    /**
     * Searches for a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user with the given username if found, or empty otherwise
     */
    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Searches for a user by their email.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user with the given email if found, or empty otherwise
     */
    @NonNull
    Optional<User> findByEmail(@NonNull String email);

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username to check
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     */
    boolean existsByUsername(@NonNull String username);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check
     * @return {@code true} if a user with the given email exists, {@code false} otherwise
     */
    boolean existsByEmail(@NonNull String email);
}
