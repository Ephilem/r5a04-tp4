package fr.ubs.sporttrack.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that processes JWT authentication for each request.
 * This filter checks the Authorization header for a valid JWT token and sets the authentication context if the token is valid.
 *
 * @author Raphael AIME
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtils.verifyToken(jwt)) {
                    Claims claims = jwtUtils.extractAllClaims(jwt);

                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(claims);
                    SecurityContextHolder.getContext().setAuthentication(authentication); // Set user is authenticated. He can now go to protected resources.
                }
            } catch (JwtException ignored) {
                // Invalid token, do nothing
            }
        }

        filterChain.doFilter(request, response);
    }
}
