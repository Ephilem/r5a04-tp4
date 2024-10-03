package fr.ubs.sporttrack.jwt;

import fr.ubs.sporttrack.controller.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * Implementation of {@link AuthenticationEntryPoint} that handles unauthorized access attempts.
 * This component logs the unauthorized access attempt and sends an appropriate error response to the client.
 *
 * @author Raphael AIME
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {} {}", authException.getMessage(), authException.getClass().getSimpleName());

        new ErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "You need to login to access this resource"
        ).writeIntoResponse(response);
    }
}
