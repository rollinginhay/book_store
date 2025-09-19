package sd_009.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sd_009.bookstore.dto.jsonApiResource.user.LoginRequest;
import sd_009.bookstore.dto.jsonApiResource.user.RegisterRequest;
import sd_009.bookstore.dto.jsonApiResource.user.AuthObject;
import sd_009.bookstore.service.auth.AuthService;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Non-OAuth authentication", description = "Auth by username and password")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(description = "Login with username and password")
    @ApiResponse(responseCode = "200", description = "Login success", content = @Content(schema = @Schema(implementation = AuthObject.class)))
    @PostMapping("/login")
    public ResponseEntity<AuthObject> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(description = "Manually register a new user")
    @ApiResponse(responseCode = "200", description = "Register success", content = @Content(schema = @Schema(implementation = AuthObject.class)))
    @PostMapping("/register")
    public ResponseEntity<AuthObject> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
