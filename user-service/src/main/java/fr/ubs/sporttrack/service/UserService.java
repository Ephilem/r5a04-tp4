package fr.ubs.sporttrack.service;

import fr.ubs.sporttrack.controller.dto.UpdateUserDTO;
import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import fr.ubs.sporttrack.model.User;
import fr.ubs.sporttrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link User} entities.
 * This service provides methods to perform CRUD operations on User entities.
 *
 * @author Raphael AIME
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a {@code UserService} with the specified {@link UserRepository}.
     * Initializes the {@link BCryptPasswordEncoder} for encoding passwords.
     *
     * @param userRepository the repository used for accessing user data
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**

     */

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return an {@code Optional} containing the user with the given ID if found, or empty otherwise
     */
    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users with pagination and sorting.
     *
     * @param pageable the pagination and sorting information
     * @return a paginated list of users
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Searches users by username with pagination and sorting.
     *
     * @param search the search query
     * @param pageable the pagination and sorting information
     * @return a paginated list of users matching the search query
     */
    public Page<User> searchUsersByUsername(String search, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(search, pageable);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return an {@code Optional} containing the found user if they exist, or empty otherwise
     */
    public Optional<User> findUsersByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to find
     * @return an {@code Optional} containing the found user if they exist, or empty otherwise
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Saves a new user.
     *
     * @param user the user to save
     * @return the saved user
     * @throws UserAlreadyExistException if a user with the same username already exists
     */
    public User saveUser(User user) throws UserAlreadyExistException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update
     * @param user the user details to update
     * @return the updated user
     * @throws UserNotFoundException if the user is not found
     */
    public User updateUser(int id, UpdateUserDTO user) throws UserNotFoundException {
        User updatedUser = new User();
        User oldUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Id '" + id + "' not found"));

        // new password
        if (!user.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword().get()));
        } else {
            updatedUser.setPassword(oldUser.getPassword());
        }

        updatedUser.setId(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        return userRepository.update(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    public void deleteUser(int id) throws UserNotFoundException {
        userRepository.deleteById(id);
    }

    /**
     * Validates user credentials.
     *
     * @param username the username
     * @param password the password
     * @return an {@code Optional} containing the user if the credentials are valid, or empty otherwise
     */
    public Optional<User> validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}
