package my.app.moment.controller;

import my.app.moment.dto.JwtAuthenticationResponse;
import my.app.moment.dto.SignInRequest;
import my.app.moment.dto.SignUpRequest;
import my.app.moment.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody SignUpRequest request) {
        logger.info("Received registration request for user: {}", request.getUsername()); // Изменяем email на username
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest request) {
        logger.info("Received login request for user: {}", request.getUsername()); // Изменяем email на username
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}