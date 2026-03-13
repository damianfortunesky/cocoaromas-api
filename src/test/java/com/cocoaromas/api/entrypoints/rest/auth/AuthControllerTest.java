package com.cocoaromas.api.entrypoints.rest.auth;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.application.port.in.auth.RegisterUseCase;
import com.cocoaromas.api.application.service.auth.EmailAlreadyRegisteredException;
import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import com.cocoaromas.api.domain.auth.RegisterCommand;
import com.cocoaromas.api.domain.auth.RegisteredUser;
import com.cocoaromas.api.domain.auth.Role;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @MockBean
    private RegisterUseCase registerUseCase;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthenticatedUser user = new AuthenticatedUser(1L, "admin@cocoaromas.local", Role.ADMIN);
        given(loginUseCase.login("admin@cocoaromas.local", "Admin123!"))
                .willReturn(new AuthToken("token-value", "Bearer", 3600, user));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@cocoaromas.local",
                                  "password": "Admin123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token-value"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.user.role").value("admin"));
    }

    @Test
    void shouldReturnAuthenticatedUser() throws Exception {
        given(getCurrentUserUseCase.getCurrentUser())
                .willReturn(new AuthenticatedUser(2L, "employee@cocoaromas.local", Role.EMPLOYEE));

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("employee@cocoaromas.local"))
                .andExpect(jsonPath("$.role").value("employee"));
    }

    @Test
    void shouldRegisterClientSuccessfully() throws Exception {
        given(registerUseCase.register(new RegisterCommand("ana@cocoaromas.local", "Ana12345")))
                .willReturn(new RegisteredUser(10L, "ana@cocoaromas.local", Role.CLIENT,
                        OffsetDateTime.parse("2026-01-01T10:00:00Z")));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "ana@cocoaromas.local",
                                  "password": "Ana12345"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.email").value("ana@cocoaromas.local"))
                .andExpect(jsonPath("$.role").value("client"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyRegistered() throws Exception {
        given(registerUseCase.register(new RegisterCommand("ana@cocoaromas.local", "Ana12345")))
                .willThrow(new EmailAlreadyRegisteredException("ana@cocoaromas.local"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "ana@cocoaromas.local",
                                  "password": "Ana12345"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_REGISTERED"));
    }
}
