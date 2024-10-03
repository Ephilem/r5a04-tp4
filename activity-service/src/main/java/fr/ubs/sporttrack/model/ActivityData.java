package fr.ubs.sporttrack.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Represents a measurement of an activity. Can be multiple per activity.
 *
 * @see Activity
 * @author Raphael AIME
 */
@Entity
@Table(name = "activity_data")
public class ActivityData {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JsonProperty("time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        @NotNull(message = "Timestamp cannot be null.")
        @Temporal(TemporalType.TIME)
        private Date timestamp;

        @JsonProperty("cardioFrequency")
        @Min(value = 40, message = "Heart rate must be at least 40 bpm")
        @Max(value = 220, message = "Heart rate must be no more than 220 bpm")
        private int heartRate;

        @JsonProperty("latitude")
        @DecimalMin(value = "-90.0", message = "Latitude must be greater than -90")
        @DecimalMax(value = "90.0", message = "Latitude must be less than 90")
        private float latitude;

        @JsonProperty("longitude")
        @DecimalMin(value = "-180.0", message = "Longitude must be greater than -180")
        @DecimalMax(value = "180.0", message = "Longitude must be less than 180")
        private float longitude;

        @JsonProperty("altitude")
        @NotNull(message = "Altitude cannot be null")
        private float altitude;

        @ManyToOne(optional = false)
        @JoinColumn(name = "activity_id", nullable = false)
        private Activity activity;

        // Getters
        public Long getId() {
                return id;
        }

        public Date getTimestamp() {
                return timestamp;
        }

        public int getHeartRate() {
                return heartRate;
        }

        public float getLatitude() {
                return latitude;
        }

        public float getLongitude() {
                return longitude;
        }

        public float getAltitude() {
                return altitude;
        }

        // Setters
        public void setTimestamp(Date timestamp) {
                this.timestamp = timestamp;
        }

        public void setHeartRate(int heartRate) {
                this.heartRate = heartRate;
        }

        public void setLatitude(float latitude) {
                this.latitude = latitude;
        }

        public void setLongitude(float longitude) {
                this.longitude = longitude;
        }

        public void setAltitude(float altitude) {
                this.altitude = altitude;
        }

        public void setActivity(Activity activity) {
                this.activity = activity;
        }

}
