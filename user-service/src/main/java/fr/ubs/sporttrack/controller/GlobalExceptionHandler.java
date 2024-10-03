package fr.ubs.sporttrack.controller;

import fr.ubs.sporttrack.exception.UserAlreadyExistException;
import fr.ubs.sporttrack.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global Exception Handler.
 * Applies to all controllers, this class handles exceptions and returns proper responses.
 *
 * @author Raphael AIME
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link MethodArgumentNotValidException} for bad requests.
     * This helps users of the API understand what is wrong with their request.
     *
     * @param ex the exception
     * @return the response entity with the error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", String.join(", ", errors)).toResponseEntity();
    }


    /**
     * Handle UserNotFoundException
     *
     * @param ex the exception
     * @return the response entity with the error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not found", ex.getMessage()).toResponseEntity();
    }

    /**
     * Handle UserAlreadyExistException
     *
     * @param ex the exception
     * @return the response entity with the error message
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage()).toResponseEntity();
    }

}