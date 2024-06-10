package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import yaremax.com.sa_task_04_06.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private User mockUser;

    @BeforeEach
    void init() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "testSecretKey012345678901234567890123456789");
    }

    @Test
    void extractSubject_shouldReturnSubject() {
        // Arrange
        String expectedSubject = "testUser";
        when(mockUser.getUsername()).thenReturn(expectedSubject);
        String jwtToken = jwtService.generateToken(mockUser);

        // Act
        String actualSubject = jwtService.extractSubject(jwtToken);

        // Assert
        assertThat(actualSubject).isEqualTo(expectedSubject);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValidAndNotExpired() {
        // Arrange
        when(mockUser.getUsername()).thenReturn("validUserEmail");
        String validToken = jwtService.generateToken(mockUser);

        // Act
        boolean isTokenValid = jwtService.isTokenValid(validToken, mockUser);

        // Assert
        assertTrue(isTokenValid);
    }

    @Test
    void generateToken_withoutExtraClaims_shouldGenerateToken() {
        // Arrange
        when(mockUser.getUsername()).thenReturn("tokenUser");

        // Act
        String generatedToken = jwtService.generateToken(mockUser);

        // Assert
        assertNotNull(generatedToken);
        assertThat(generatedToken).isNotEmpty();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        // Arrange
        when(mockUser.getUsername()).thenReturn("email");
        String tokenForOriginalUser = jwtService.generateToken(mockUser);

        User anotherMockUser = new User();
        anotherMockUser.setEmail("diffEmail");

        // Act
        boolean isTokenValid = jwtService.isTokenValid(tokenForOriginalUser, anotherMockUser);

        // Assert
        assertFalse(isTokenValid);
    }
}