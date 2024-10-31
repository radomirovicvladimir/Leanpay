package org.leanpay.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Read the secret key from the application.properties file
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes()); // Create a new secret key
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder() // Create a new JWT builder
                .setSubject(username) // Set the subject of the token
                .claim("role", role) // Add a custom claim
                .setIssuedAt(now) // Set the time of token issuance
                .setExpiration(expiryDate) // Set the expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Set the signature algorithm and secret key
                .compact(); // Build the token
    }
}