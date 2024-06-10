package yaremax.com.sa_task_04_06.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * The JWT auth token.
     */
    private String token;
}
