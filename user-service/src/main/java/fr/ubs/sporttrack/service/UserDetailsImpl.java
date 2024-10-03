package fr.ubs.sporttrack.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ubs.sporttrack.controller.dto.UserDTO;
import fr.ubs.sporttrack.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link UserDetails} for Spring Security.
 * This class represents the authenticated user and provides user-related data.
 *
 * @author Raphael AIME
 */
public class UserDetailsImpl implements UserDetails {

    private final int id;
    private final String username;
    private final String email;

    @JsonIgnore
    private String password;

    /**
     * Constructs a {@code UserDetailsImpl} with the specified details.
     *
     * @param id the user ID
     * @param username the username
     * @param email the email address
     * @param password the password
     */
    public UserDetailsImpl(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Builds a {@code UserDetailsImpl} instance from a {@link User} entity.
     *
     * @param user the user entity
     * @return the {@code UserDetailsImpl} instance
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    /**
     * Returns the user's authorities. In this implementation, it returns an empty list.
     *
     * @return the user's authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the user's username.
     *
     * @return the user's username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Indicates whether the user's account has expired.
     * In this implementation, it always returns {@code true}.
     *
     * @return {@code true} if the user's account is non-expired, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * In this implementation, it always returns {@code true}.
     *
     * @return {@code true} if the user is non-locked, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * In this implementation, it always returns {@code true}.
     *
     * @return {@code true} if the user's credentials are non-expired, {@code false} otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * In this implementation, it always returns {@code true}.
     *
     * @return {@code true} if the user is enabled, {@code false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Compares this user to the specified object. The result is {@code true} if and only if the argument is not {@code null}
     * and is a {@code UserDetailsImpl} object that has the same id as this object.
     *
     * @param o the object to compare this {@code UserDetailsImpl} against
     * @return {@code true} if the given object represents a {@code UserDetailsImpl} equivalent to this user, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    /**
     * Convert the object to a {@link fr.ubs.sporttrack.controller.dto.UserDTO}
     *
     * @return the converted object
     */
    public UserDTO toUserDTO() {
        return new UserDTO(id, username, email);
    }
}
