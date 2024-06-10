package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.dto.AuthenticationRequest;
import yaremax.com.sa_task_04_06.dto.AuthenticationResponse;
import yaremax.com.sa_task_04_06.entity.User;


/**
 * This class provides service for user authentication.
 * It implements registration and login functionality.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param  request  the {@link AuthenticationRequest} DTO
     * @return          the {@link AuthenticationResponse} DTO with JWT token
     */
    public AuthenticationResponse register(AuthenticationRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userService.createUser(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    /**
     * Logs in an existing user.
     *
     * @param  request  the {@link AuthenticationRequest} DTO
     * @return          the {@link AuthenticationResponse} DTO with JWT token
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        // will also check if username and password are correct
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        User user = userService.findUserByEmail(request.getEmail());

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
