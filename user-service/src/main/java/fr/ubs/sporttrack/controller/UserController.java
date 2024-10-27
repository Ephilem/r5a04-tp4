package fr.ubs.sporttrack.controller;

import fr.ubs.sporttrack.controller.dto.CreateUserDTO;
import fr.ubs.sporttrack.controller.dto.UpdateUserDTO;
import fr.ubs.sporttrack.controller.dto.UserDTO;
import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import fr.ubs.sporttrack.model.User;
import fr.ubs.sporttrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD Controller for managing {@link User} entities.
 * Provides endpoints to create, retrieve, and delete users.
 *
 * @author Raphael AIME
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a {@code UserController} with the specified {@link UserService}.
     *
     * @param userService the service used to manage users
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieve all users with pagination, sorting, and searching
     *
     * @param pageable pagination and sorting information
     * @param search query to search users by username or email
     * @return ResponseEntity containing the paginated list of users
     */
    @Operation(summary = "Retrieve all users with pagination, sorting, and searching", tags = {"User Management"})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search) {

        Page<UserDTO> users = (search == null || search.isBlank()) ?
                userService.findAll(pageable) :
                userService.searchUsersByUsername(search, pageable);

        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return {@code ResponseEntity} containing the user or an error message
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Retrieve a user by ID", tags = {"User Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) throws UserNotFoundException {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return {@code ResponseEntity} containing the created user or an error message
     * @throws UserAlreadyExistException if the user already exists
     */
    @Operation(summary = "Create a new user", tags = {"User Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict: User already exists")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO user) throws UserAlreadyExistException {
        return new ResponseEntity<>(userService.saveUser(user.toUser()), HttpStatus.CREATED);
    }

    /**
     * Updates a user by their ID.
     *
     * @param id the user ID
     * @param user the user details to update
     * @return {@code ResponseEntity} containing the updated user or an error message
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Update a user", tags = {"User Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserDTO user) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID
     * @return {@code ResponseEntity} with no content or an error message
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Delete a user", tags = {"User Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}