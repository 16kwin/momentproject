package my.app.moment.service;

import lombok.RequiredArgsConstructor;
import my.app.moment.JWT.JwtService;
import my.app.moment.dto.JwtAuthenticationResponse;
import my.app.moment.dto.SignInRequest;
import my.app.moment.dto.SignUpRequest;
import my.app.moment.model.Role;
import my.app.moment.model.User;
import my.app.moment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        try {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : Role.USER)
                    .build();
            userRepository.save(user);
            logger.info("User {} registered successfully", user.getUsername());
            String jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build(); // Исправлено здесь
        } catch (Exception e) {
            logger.error("Error during registration for user {}", request.getUsername(), e);
            throw new RuntimeException("Registration failed", e);
        }
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
            String jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build(); // Исправлено здесь
        } catch (Exception e) {
            logger.error("Error during signin for user {}", request.getUsername(), e);
            throw new RuntimeException("Signin failed", e);
        }
    }
}