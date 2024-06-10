package yaremax.com.sa_task_04_06.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for user authentication.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    /**
     * The user's email.
     */
    @Schema(description = "The user's email",
            example = "john.doe@example.com")
    private String email;

    /**
     * The user's password.
     */
    @Schema(description = "The user's password",
            example = "mySecurePassword123!")
    private String password;
}
