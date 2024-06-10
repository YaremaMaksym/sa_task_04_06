package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import yaremax.com.sa_task_04_06.dto.AuthenticationRequest;
import yaremax.com.sa_task_04_06.dto.AuthenticationResponse;
import yaremax.com.sa_task_04_06.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(jwtService, userService, passwordEncoder, authenticationManager);
    }

    @Nested
    class RegisterTests {
        @Test
        void register_shouldCreateUserAndGenerateToken() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
            User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            String jwtToken = "generatedToken";

            when(passwordEncoder.encode(request.getPassword())).thenReturn(user.getPassword());
            when(jwtService.generateToken(user)).thenReturn(jwtToken);

            // Act
            AuthenticationResponse response = authenticationService.register(request);

            // Assert
            assertThat(response.getToken()).isEqualTo(jwtToken);
            verify(userService).createUser(user);
            verify(jwtService).generateToken(user);
        }
    }

    @Nested
    class LoginTests {
        @Test
        void login_shouldAuthenticateAndGenerateToken() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
            User user = User.builder()
                    .email(request.getEmail())
                    .password("encodedPassword")
                    .build();
            String jwtToken = "generatedToken";

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            when(userService.findUserByEmail(request.getEmail())).thenReturn(user);
            when(jwtService.generateToken(user)).thenReturn(jwtToken);

            // Act
            AuthenticationResponse response = authenticationService.login(request);

            // Assert
            assertThat(response.getToken()).isEqualTo(jwtToken);
            verify(authenticationManager).authenticate(authToken);
            verify(userService).findUserByEmail(request.getEmail());
            verify(jwtService).generateToken(user);
        }

        @Test
        void login_shouldThrowBadCredentialsException() {
            // Arrange
            AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongPassword");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            when(authenticationManager.authenticate(authToken)).thenThrow(BadCredentialsException.class);

            // Act & Assert
            assertThatExceptionOfType(BadCredentialsException.class)
                    .isThrownBy(() -> authenticationService.login(request));

            verify(authenticationManager).authenticate(authToken);
            verifyNoInteractions(userService, jwtService);
        }
    }
}