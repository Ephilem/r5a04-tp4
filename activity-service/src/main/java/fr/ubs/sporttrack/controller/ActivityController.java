package fr.ubs.sporttrack.controller;

import fr.ubs.sporttrack.controller.dto.CreateActivityDTO;
import fr.ubs.sporttrack.exception.ActivityNotFoundException;
import fr.ubs.sporttrack.exception.ActivityAlreadyExistException;
import fr.ubs.sporttrack.model.Activity;
import fr.ubs.sporttrack.service.ActivityService;
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

import java.util.List;

/**
 * CRUD Controller for activities
 * Provides endpoints to manage {@link Activity} entities
 *
 * @author Raphael AIME
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * Retrieve all activities with pagination, sorting, and searching
     *
     * @param pageable pagination and sorting information
     * @param search query to search activities by description
     * @return ResponseEntity containing the paginated list of activities
     */
    @Operation(summary = "Retrieve all activities with pagination, sorting, and searching", tags = {"Activity Management"})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of activities",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<Activity>> getAllActivities(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search) {

        Page<Activity> activities = (search == null || search.isBlank()) ?
                activityService.findAll(pageable) :
                activityService.searchActivities(search, pageable);

        return ResponseEntity.ok(activities);
    }

    /**
     * Retrieve an activity by ID
     *
     * @param id the activity id
     * @return ResponseEntity containing the activity or an error message
     */
    @Operation(summary = "Retrieve an activity by ID", tags = {"Activity Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Activity.class))),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getActivityById(@PathVariable long id) throws ActivityNotFoundException {
        return activityService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ActivityNotFoundException(id));
    }

    /**
     * Create a new activity. An ID will be generated automatically
     *
     * @param activity the activity to create
     * @return ResponseEntity containing the created activity or an error message
     */
    @Operation(summary = "Create a new activity", tags = {"Activity Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activity created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateActivityDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflict: Activity already exists")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createActivity(@Valid @RequestBody CreateActivityDTO activity) throws ActivityAlreadyExistException {
        return new ResponseEntity<>(activityService.save(activity.toActivity()), HttpStatus.CREATED);
    }

    /**
     * Update an existing activity
     *
     * @param id the activity id
     * @param activity the activity details to update
     * @return ResponseEntity containing the updated activity or an error message
     */
    @Operation(summary = "Update an existing activity", tags = {"Activity Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Activity.class))),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateActivity(@PathVariable long id, @Valid @RequestBody Activity activity) throws ActivityNotFoundException {
        activity.setId(id);

        return new ResponseEntity<>(activityService.update(activity), HttpStatus.OK);
    }

    /**
     * Delete an activity
     *
     * @param id the activity id
     * @return ResponseEntity with no content or an error message
     */
    @Operation(summary = "Delete an activity", tags = {"Activity Management"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Activity successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable int id) throws ActivityNotFoundException {
        activityService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
