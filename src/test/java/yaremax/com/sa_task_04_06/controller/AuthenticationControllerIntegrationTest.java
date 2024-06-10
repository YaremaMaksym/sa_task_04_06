package yaremax.com.sa_task_04_06.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.SaTask0406Application;
import yaremax.com.sa_task_04_06.dto.AuthenticationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = SaTask0406Application.class)
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String sendRegisterUserRequest(String email, String password) throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        String jsonRequest = objectMapper.writeValueAsString(request);

        return mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
    }

    @Nested
    class RegisterTests {
        @Test
        void register_ShouldReturnAuthenticationResponse() throws Exception {
            sendRegisterUserRequest("newuser@example.com", "newpassword");
        }

        @Test
        void register_ShouldReturnBadRequest_WhenRequestBodyIsMissing() throws Exception {
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void register_ShouldReturnConflict_WhenUserAlreadyExists() throws Exception {
            sendRegisterUserRequest("user@example.com", "password");

            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new AuthenticationRequest("user@example.com", "password"))))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class LoginTests {
        @Test
        void login_ShouldReturnAuthenticationResponse_WhenCredentialsAreValid() throws Exception {
            sendRegisterUserRequest("user@example.com", "password");

            AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
            AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongpassword");
            String jsonRequest = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void login_ShouldReturnBadRequest_WhenRequestBodyIsMissing() throws Exception {
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}