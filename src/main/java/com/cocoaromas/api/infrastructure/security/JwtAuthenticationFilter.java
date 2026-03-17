package com.cocoaromas.api.infrastructure.security;

import com.cocoaromas.api.domain.auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            try {
                Claims claims = jwtTokenProvider.parseClaims(token);
                Long userId = parseUserId(claims);
                Role role = parseRole(claims);
                String email = claims.get("email", String.class);

                UserPrincipal principal = new UserPrincipal(userId, email, role);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
                log.debug("JWT authentication skipped: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private Long parseUserId(Claims claims) {
        String subject = claims.getSubject();
        if (subject != null && !subject.isBlank()) {
            return Long.valueOf(subject.trim());
        }

        Object fallbackUserId = claims.get("userId");
        if (fallbackUserId instanceof Number number) {
            return number.longValue();
        }
        if (fallbackUserId instanceof String value && !value.isBlank()) {
            return Long.valueOf(value.trim());
        }

        throw new IllegalArgumentException("Missing subject/userId in JWT");
    }

    private Role parseRole(Claims claims) {
        Role roleFromRoleClaim = parseRoleValue(claims.get("role"));
        if (roleFromRoleClaim != null) {
            return roleFromRoleClaim;
        }

        Object authorities = claims.get("authorities");
        if (authorities instanceof Collection<?> values) {
            for (Object value : values) {
                Role role = parseRoleValue(value);
                if (role != null) {
                    return role;
                }
            }
        }

        if (authorities instanceof String rawAuthorities && !rawAuthorities.isBlank()) {
            return Arrays.stream(rawAuthorities.split(","))
                    .map(String::trim)
                    .map(this::parseRoleValue)
                    .filter(role -> role != null)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Missing role/authorities in JWT"));
        }

        throw new IllegalArgumentException("Missing role/authorities in JWT");
    }

    private Role parseRoleValue(Object rawRole) {
        if (rawRole instanceof Map<?, ?> roleMap) {
            Object authority = roleMap.get("authority");
            if (authority != null) {
                return parseRoleValue(authority);
            }
        }

        if (!(rawRole instanceof String roleValue) || roleValue.isBlank()) {
            return null;
        }

        String normalized = roleValue.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring("ROLE_".length());
        }

        try {
            return Role.valueOf(normalized);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
