package com.cocoaromas.api.infrastructure.security;

import com.cocoaromas.api.application.port.out.auth.TokenProviderPort;
import com.cocoaromas.api.domain.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProviderPort {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.expirationSeconds());

        return Jwts.builder()
                .subject(String.valueOf(user.id()))
                .issuer(jwtProperties.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("role", user.role().name())
                .claim("email", user.email())
                .claim("name", user.name())
                .signWith(secretKey)
                .compact();
    }

    @Override
    public long getAccessTokenExpirationSeconds() {
        return jwtProperties.expirationSeconds();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
