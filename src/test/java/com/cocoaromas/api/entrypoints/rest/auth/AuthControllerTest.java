package com.cocoaromas.api.entrypoints.rest.auth;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cocoaromas.api.application.port.in.auth.GetCurrentUserUseCase;
import com.cocoaromas.api.application.port.in.auth.LoginUseCase;
import com.cocoaromas.api.domain.auth.AuthToken;
import com.cocoaromas.api.domain.auth.AuthenticatedUser;
import com.cocoaromas.api.domain.auth.Role;
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

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthenticatedUser user = new AuthenticatedUser(1L, "Admin User", "admin@cocoaromas.local", Role.ADMIN);
        given(loginUseCase.login("admin", "Admin123!"))
                .willReturn(new AuthToken("token-value", "Bearer", 3600, user));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "emailOrUsername": "admin",
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
                .willReturn(new AuthenticatedUser(2L, "Employee User", "employee@cocoaromas.local", Role.EMPLOYEE));

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("employee@cocoaromas.local"))
                .andExpect(jsonPath("$.role").value("employee"));
    }
}
