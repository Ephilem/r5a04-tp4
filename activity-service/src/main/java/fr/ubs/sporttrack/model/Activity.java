package fr.ubs.sporttrack.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Define Activity entity
 *
 * @author Raphael AIME
 */
@Entity
@Table(name = "activities")
public class Activity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "Date cannot be null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @Temporal(TemporalType.DATE)
        private Date date;

        @NotBlank(message = "Description cannot be blank")
        @Column(nullable = false)
        private String description;

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
        @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
        @NotNull(message = "Data points cannot be null")
        @Size(min = 1, message = "At least one data point is required")
        private Set<ActivityData> points;

        // Getters
        public Long getId() {
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

        public Set<ActivityData> getPoints() {
                return points;
        }

        // Setters
        public void setId(Long id) {
                this.id = id;
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

        public Activity setPoints(Set<ActivityData> points) {
                this.points = points;
                return this;
        }
}

