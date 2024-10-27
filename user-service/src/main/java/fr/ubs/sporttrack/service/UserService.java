package fr.ubs.sporttrack.service;

import fr.ubs.sporttrack.controller.dto.UpdateUserDTO;
import fr.ubs.sporttrack.controller.dto.UserDTO;
import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import fr.ubs.sporttrack.model.User;
import fr.ubs.sporttrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link User} entities.
 * This service provides methods to perform CRUD operations on User entities.
 *
 * @author Raphael AIME
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a {@code UserService} with the specified {@link UserRepository}.
     * Initializes the {@link PasswordEncoder} for encoding passwords.
     *
     * @param userRepository the repository used for accessing user data
     * @param passwordEncoder the encoder used for password hashing
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return an {@code Optional} containing the user with the given ID if found, or empty otherwise
     */
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUser(int id) {
        return userRepository.findById(id)
                .map(UserDTO::new);
    }

    /**
     * Retrieves all users with pagination and sorting.
     *
     * @param pageable the pagination and sorting information
     * @return a paginated list of users
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PageImpl<>(
                users.getContent().stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList()),
                pageable,
                users.getTotalElements()
        );
    }

    /**
     * Searches users by username with pagination and sorting.
     *
     * @param search the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of users matching the search query
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsersByUsername(String search, Pageable pageable) {
        Page<User> users = userRepository.findByUsernameContainingIgnoreCase(search, pageable);
        return new PageImpl<>(
                users.getContent().stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList()),
                pageable,
                users.getTotalElements()
        );
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return an {@code Optional} containing the found user if they exist, or empty otherwise
     */
    @Cacheable(key = "#username")
    @Transactional(readOnly = true)
    public Optional<UserDTO> findUsersByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDTO::new);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to find
     * @return an {@code Optional} containing the found user if they exist, or empty otherwise
     */
    @Cacheable(key = "#email")
    @Transactional(readOnly = true)
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDTO::new);
    }

    /**
     * Saves a new user.
     *
     * @param user the user to save
     * @return the saved user
     * @throws UserAlreadyExistException if a user with the same username already exists
     */
    @CacheEvict(allEntries = true)
    @Transactional
    public UserDTO saveUser(User user) throws UserAlreadyExistException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        CompletableFuture<String> encodedPassword = CompletableFuture
                .supplyAsync(() -> passwordEncoder.encode(user.getPassword()));

        user.setPassword(encodedPassword.join());
        return new UserDTO(userRepository.save(user));
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update
     * @param user the user details to update
     * @return the updated user
     * @throws UserNotFoundException if the user is not found
     */
    @CacheEvict(key = "#id")
    @Transactional
    public UserDTO updateUser(int id, UpdateUserDTO user) throws UserNotFoundException {
        User updatedUser = new User();
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Id '" + id + "' not found"));

        if (user.getPassword().isPresent()) {
            CompletableFuture<String> encodedPassword = CompletableFuture
                    .supplyAsync(() -> passwordEncoder.encode(user.getPassword().get()));
            updatedUser.setPassword(encodedPassword.join());
        } else {
            updatedUser.setPassword(oldUser.getPassword());
        }

        updatedUser.setId(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        return new UserDTO(userRepository.save(updatedUser));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteUser(int id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Validates user credentials.
     *
     * @param username the username
     * @param password the password
     * @return an {@code Optional} containing the user if the credentials are valid, or empty otherwise
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            CompletableFuture<Boolean> isValid = CompletableFuture
                    .supplyAsync(() -> passwordEncoder.matches(password, user.get().getPassword()));

            if (isValid.join()) {
                return Optional.of(new UserDTO(user.get()));
            }
        }
        return Optional.empty();
    }
}
