package fr.ubs.sporttrack.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT operations such as creating, parsing, and validating tokens.
 * This class provides methods to extract claims, verify tokens, and check token expiration.
 *
 * @author Raphael AIME
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Verifies the secret is not empty.
     */
    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new RuntimeException("Invalid secret for JWT");
        }
    }

    /**
     * Generates the signing key from the secret.
     *
     * @return the {@link SecretKey} used for signing the JWT
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verifies if the JWT token is valid and not expired.
     *
     * @param token the JWT token
     * @return {@code true} if the token is valid and not expired, {@code false} otherwise
     */
    public boolean verifyToken(String token) {
        // Because isTokenExpired will use the method extractAllClaims that
        // decrypt the token, we can use it to verify the token AND if the token is expired
        return !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return {@code true} if the token is expired, {@code false} otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param <T> the type of the claim
     * @param token the JWT token
     * @param claimsResolver a function to extract the claim from the token
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
