package yaremax.com.sa_task_04_06.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaremax.com.sa_task_04_06.dto.AuthenticationRequest;
import yaremax.com.sa_task_04_06.dto.AuthenticationResponse;
import yaremax.com.sa_task_04_06.exception.custom.ApiException;
import yaremax.com.sa_task_04_06.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Controller for user authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Register a new user with the provided credentials")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationResponse.class))})
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Login an existing user", description = "Login an existing user with the provided credentials")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationResponse.class))})
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}