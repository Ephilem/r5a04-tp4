package fr.ubs.sporttrack.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Authentication token for JWT-based authentication.
 * This class holds the JWT claims and provides methods to access the claims, credentials, and principal.
 * Used by the {@link AuthTokenFilter} to set who is authenticated.
 *
 * @author Raphael AIME
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Claims claims;

    /**
     * Constructs a {@code JwtAuthenticationToken} with the specified JWT claims.
     * Sets the authentication status to true.
     *
     * @param claims the JWT claims
     */
    public JwtAuthenticationToken(Claims claims) {
        super(null);
        this.claims = claims;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return claims;
    }

    public Claims getClaims() {
        return claims;
    }

}
