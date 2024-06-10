package yaremax.com.sa_task_04_06.dto;

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
    private String email;

    /**
     * The user's password.
     */
    private String password;
}
