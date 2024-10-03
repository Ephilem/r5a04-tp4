package fr.ubs.sporttrack.controller;

import fr.ubs.sporttrack.controller.dto.AuthResponseDTO;
import fr.ubs.sporttrack.jwt.JwtUtils;
import fr.ubs.sporttrack.controller.dto.AuthRequestDTO;
import fr.ubs.sporttrack.service.UserDetailsImpl;
import fr.ubs.sporttrack.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param authenticationRequest the authentication request containing username and password
     * @return {@code ResponseEntity} containing the JWT token or an error message
     * @throws Exception if an authentication error occurs
     */
    @Operation(summary = "Authenticate a user and return a JWT token", tags = {"Authentication"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid username or password")
    })
    @PostMapping(path= "/authenticate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequestDTO authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            return new ErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    "Invalid username or password"
            ).toResponseEntity();
        }

        // We are sure the cast will work because the authentication is configured to use UserDetailsImpl
        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponseDTO(jwt, userDetails.toUserDTO()));
    }
}
