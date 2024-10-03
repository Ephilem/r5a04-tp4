package fr.ubs.sporttrack.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ubs.sporttrack.model.Activity;
import fr.ubs.sporttrack.model.ActivityData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for creating an {@link Activity}.
 * This DTO is used to transfer data from the client to the server when creating a new activity.
 *
 * @author Raphael AIME
 */
public class CreateActivityDTO {

    @JsonProperty("date")
    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date date;

    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @JsonProperty("distance")
    @Min(value = 1, message = "Distance must be greater than zero")
    private int distance; // in meters

    @JsonProperty("freq_min")
    @Min(value = 1, message = "Minimum frequency must be greater than zero")
    @Max(value = 250, message = "Minimum frequency must be less than or equal to 250")
    private int freqMin;

    @JsonProperty("freq_max")
    @Min(value = 1, message = "Maximum frequency must be greater than zero")
    @Max(value = 250, message = "Maximum frequency must be less than or equal to 250")
    private int freqMax;

    @JsonProperty("data")
    @NotNull(message = "Data points cannot be null")
    @Size(min = 1, message = "At least one data point is required")
    private Set<ActivityData> points;

    // Getters and setters

    /**
     * Returns the date of the activity.
     *
     * @return the date of the activity
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the activity.
     *
     * @param date the date of the activity
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the description of the activity.
     *
     * @return the description of the activity
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the activity.
     *
     * @param description the description of the activity
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the distance of the activity.
     *
     * @return the distance of the activity
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets the distance of the activity.
     *
     * @param distance the distance of the activity
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Returns the minimum frequency of the activity.
     *
     * @return the minimum frequency of the activity
     */
    public int getFreqMin() {
        return freqMin;
    }

    /**
     * Sets the minimum frequency of the activity.
     *
     * @param freqMin the minimum frequency of the activity
     */
    public void setFreqMin(int freqMin) {
        this.freqMin = freqMin;
    }

    /**
     * Returns the maximum frequency of the activity.
     *
     * @return the maximum frequency of the activity
     */
    public int getFreqMax() {
        return freqMax;
    }

    /**
     * Sets the maximum frequency of the activity.
     *
     * @param freqMax the maximum frequency of the activity
     */
    public void setFreqMax(int freqMax) {
        this.freqMax = freqMax;
    }

    /**
     * Returns the data points of the activity.
     *
     * @return the data points of the activity
     */
    public Set<ActivityData> getPoints() {
        return points;
    }

    /**
     * Sets the data points of the activity.
     *
     * @param points the data points of the activity
     */
    public void setPoints(Set<ActivityData> points) {
        this.points = points;
    }

    /**
     * Converts this DTO to an {@link Activity} entity.
     * Note that this method does not set the ID of the activity.
     *
     * @return the {@code Activity} entity without the ID
     */
    public Activity toActivity() {
        return new Activity()
                .setDate(date)
                .setDescription(description)
                .setDistance(distance)
                .setFreqMin(freqMin)
                .setFreqMax(freqMax)
                .setPoints(points);
    }
}
