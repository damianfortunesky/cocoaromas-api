package com.cocoaromas.api.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.FilterChain;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenProvider);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateTokenWithPrefixedRole() throws Exception {
        Claims claims = new DefaultClaims();
        claims.setSubject("1");
        claims.put("role", "ROLE_ADMIN");
        claims.put("email", "admin@cocoaromas.local");

        authenticateWithClaims(claims);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void shouldAuthenticateTokenWithLowercaseRole() throws Exception {
        Claims claims = new DefaultClaims();
        claims.setSubject("1");
        claims.put("role", "admin");
        claims.put("email", "admin@cocoaromas.local");

        authenticateWithClaims(claims);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void shouldAuthenticateTokenUsingAuthoritiesClaimWhenRoleClaimIsMissing() throws Exception {
        Claims claims = new DefaultClaims();
        claims.setSubject("1");
        claims.put("authorities", List.of("ROLE_ADMIN"));
        claims.put("email", "admin@cocoaromas.local");

        authenticateWithClaims(claims);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void shouldAuthenticateTokenUsingUserIdClaimWhenSubjectIsMissing() throws Exception {
        Claims claims = new DefaultClaims();
        claims.put("userId", 1);
        claims.put("role", "ADMIN");
        claims.put("email", "admin@cocoaromas.local");

        authenticateWithClaims(claims);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    private void authenticateWithClaims(Claims claims) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token-value");
        given(jwtTokenProvider.parseClaims("token-value")).willReturn(claims);

        filter.doFilter(request, new MockHttpServletResponse(), mock(FilterChain.class));
    }
}
