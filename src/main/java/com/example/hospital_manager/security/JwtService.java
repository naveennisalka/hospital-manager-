package com.example.hospital_manager.security;

import com.example.hospital_manager.model.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Creates and validates JWTs.
 * signature = HMAC-SHA256(header + "." + payload, secret key). Only the server knows the key,
 * so if anyone changes the payload (e.g. role PATIENT -> ADMIN) the signature no longer matches.
 */
@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    /** Payload: { "sub": username, "role": ROLE, "iat": ..., "exp": ... } */
    public String generateToken(AppUser user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Checks the signature and expiry, then returns the username. Throws JwtException if invalid. */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
