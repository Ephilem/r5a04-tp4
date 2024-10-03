package fr.ubs.sporttrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Date;

/**
 * Represents an error response to be sent to the client.
 * This class includes information such as timestamp, status code, error, and message.
 * Provides methods to convert the response to JSON format and write it to an {@link HttpServletResponse}.
 *
 * @author Raphael AIME
 */
public class ErrorResponse {

    private final Date timestamp;
    private int status;
    private String error;
    private String message;

    /**
     * Constructs an {@code ErrorResponse} with the specified status, error, and message.
     * Initializes the timestamp to the current date and time.
     *
     * @param status the HTTP status code
     * @param error the error type
     * @param message the error message
     */
    public ErrorResponse(int status, String error, String message) {
        // ISO format
        this.timestamp = new Date();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Converts this {@code ErrorResponse} to a JSON string.
     *
     * @return the JSON string representation of this error response
     */
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "{\"timestamp\":\"" + timestamp + "\",\"status\":" + status + ",\"error\":\"" + error + "\",\"message\":\"" + message + "\"}";
        }
    }

    /**
     * Writes this {@code ErrorResponse} to the provided {@link HttpServletResponse}.
     *
     * @param response the {@code HttpServletResponse} to write the error response to
     * @throws IOException if an error occurs while writing the response
     */
    public void writeIntoResponse(HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(toJson());
    }

    /**
     * Converts this {@code ErrorResponse} to a {@link ResponseEntity}.
     *
     * @return the {@code ResponseEntity} representation of this error response
     */
    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}