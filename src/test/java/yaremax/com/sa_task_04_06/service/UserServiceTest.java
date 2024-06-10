package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.entity.User;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Nested
    class FindUserByEmailTests {
        @Test
        void findUserByEmail_shouldReturnUser_whenUserExists() {
            // Arrange
            String email = "user@example.com";
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .email(email)
                    .password("password")
                    .build();

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            // Act
            User foundUser = userService.findUserByEmail(email);

            // Assert
            assertThat(foundUser).isEqualTo(user);
            verify(userRepository).findByEmail(email);
        }

        @Test
        void findUserByEmail_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
            // Arrange
            String email = "nonexistent@example.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> userService.findUserByEmail(email))
                    .withMessageContaining(email);

            verify(userRepository).findByEmail(email);
        }
    }

    @Nested
    class CreateUserTests {
        @Test
        void createUser_shouldCreateUser_whenUserDoesNotExist() {
            // Arrange
            User user = User.builder()
                    .email("newuser@example.com")
                    .password("password")
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

            // Act
            userService.createUser(user);

            // Assert
            verify(userRepository).findByEmail(user.getEmail());
            verify(userRepository).save(user);
        }

        @Test
        void createUser_shouldThrowDuplicateResourceException_whenUserAlreadyExists() {
            // Arrange
            User user = User.builder()
                    .email("user@example.com")
                    .password("password")
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> userService.createUser(user))
                    .withMessageContaining(user.getEmail());

            verify(userRepository).findByEmail(user.getEmail());
            verify(userRepository, never()).save(any(User.class));
        }
    }
}