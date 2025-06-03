package com.dataquadinc.service;

import com.dataquadinc.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;
    private final long validityInMilliseconds = 1800000; // 30 minutes

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a new JWT token
    public String generateToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username (email) from token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Validate token - structure, signature, expiration
    public boolean validateToken(String token, String email) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing or empty.");
        }

        try {
            String extractedEmail = extractUsername(token);
            if (!extractedEmail.equals(email)) {
                throw new InvalidTokenException("Token does not belong to the given email.");
            }

            if (isTokenExpired(token)) {
                throw new InvalidTokenException("Token has expired.");
            }

            return true;
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token: " + e.getMessage());
        }
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Extract claims from token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
