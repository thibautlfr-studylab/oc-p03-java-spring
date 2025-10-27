package com.openclassrooms.chatop.api.service.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

/**
 * Service interface for JWT token operations.
 * Defines the contract for generating, validating, and extracting information from JWT tokens.
 */
public interface IJwtService {

    /**
     * Extract the username (email) from the JWT token.
     *
     * @param token the JWT token
     * @return the username (email)
     */
    String extractUsername(String token);

    /**
     * Extract a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver function to extract the desired claim
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generate a JWT token for the given user.
     *
     * @param userDetails the user details
     * @return the generated JWT token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generate a JWT token with extra claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param userDetails the user details
     * @return the generated JWT token
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Validate if the token is valid for the given user.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
