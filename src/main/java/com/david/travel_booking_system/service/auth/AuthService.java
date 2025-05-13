package com.david.travel_booking_system.service.auth;

import com.david.travel_booking_system.dto.request.auth.LoginRequestDTO;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.response.auth.JwtResponseDTO;
import com.david.travel_booking_system.security.TokenType;
import com.david.travel_booking_system.exception.InvalidTokenException;
import com.david.travel_booking_system.exception.MissingTokenException;
import com.david.travel_booking_system.model.Token;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.TokenRepository;
import com.david.travel_booking_system.security.CustomUserDetails;
import com.david.travel_booking_system.security.jwt.JwtService;
import com.david.travel_booking_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       CustomUserDetailsService userDetailsService, UserService userService,
                       TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public JwtResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        // Delegate User creation to UserService
        User user = userService.createUser(registerRequestDTO);

        // Wrap the User in a CustomUserDetails object and generate tokens
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Save the access token for revocation support
        saveUserToken(user, accessToken);

        // Return both tokens
        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public JwtResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        // Create an authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword()
        );

        // AuthManager verifies the credentials
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Set this authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load user and generate fresh tokens
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Revoke old access tokens save new access token
        revokeAllUserTokens(userDetails.getUser());
        saveUserToken(userDetails.getUser(), accessToken);

        // Return both tokens
        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String token) {
        tokenRepository.save(
                Token.builder()
                        .user(user)
                        .token(token)
                        .tokenType(TokenType.ACCESS)
                        .expired(false)
                        .revoked(false)
                        .build()
        );
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public JwtResponseDTO refreshToken(HttpServletRequest request) {
        // Extract the raw “Bearer <token>” header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingTokenException("No refresh token provided");
        }

        // Extract the token from the header
        String refreshToken = authHeader.substring(7);

        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            // Malformed token → 401 Unauthorized
            throw new MissingTokenException("Malformed refresh token");
        }

        // Load the user details
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // Verify the refresh token’s signature and expiry
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Generate a new access token
        String newAccessToken = jwtService.generateToken(userDetails);

        // Revoke old access tokens save new access token
        revokeAllUserTokens(userDetails.getUser());
        saveUserToken(userDetails.getUser(), newAccessToken);

        // Return both tokens
        return JwtResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // keep the same refresh token
                .build();
    }
}
