package fr.ubs.sporttrack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.*;

/**
 * Define Activity entity
 *
 * @author Raphael AIME
 */
public class Activity {
        @JsonProperty("id")
        @Min(value = 1, message = "ID must be positive")
        private int id;

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
        private List<ActivityData> points;

        // Getters
        public int getId() {
                return id;
        }

        public Date getDate() {
                return date;
        }

        public String getDescription() {
                return description;
        }

        public int getDistance() {
                return distance;
        }

        public int getFreqMin() {
                return freqMin;
        }

        public int getFreqMax() {
                return freqMax;
        }

        public List<ActivityData> getPoints() {
                return points;
        }

        // Setters
        public Activity setId(int id) {
                this.id = id;
                return this;
        }

        public Activity setDate(Date date) {
                this.date = date;
                return this;
        }

        public Activity setDescription(String description) {
                this.description = description;
                return this;
        }

        public Activity setDistance(int distance) {
                this.distance = distance;
                return this;
        }

        public Activity setFreqMin(int freqMin) {
                this.freqMin = freqMin;
                return this;
        }

        public Activity setFreqMax(int freqMax) {
                this.freqMax = freqMax;
                return this;
        }

        public Activity setPoints(List<ActivityData> points) {
                this.points = points;
                return this;
        }
}

