package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import yaremax.com.sa_task_04_06.entity.User;
import yaremax.com.sa_task_04_06.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Nested
    class LoadUserByUsernameTests {
        @Test
        void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
            // Arrange
            String email = "user@example.com";
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .email(email)
                    .password("password")
                    .build();

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            // Act
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Assert
            assertThat(userDetails.getUsername()).isEqualTo(email);
            assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
            verify(userRepository).findByEmail(email);
        }

        @Test
        void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
            // Arrange
            String email = "nonexistent@example.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(UsernameNotFoundException.class)
                    .isThrownBy(() -> userDetailsService.loadUserByUsername(email))
                    .withMessageContaining(email);

            verify(userRepository).findByEmail(email);
        }
    }
}