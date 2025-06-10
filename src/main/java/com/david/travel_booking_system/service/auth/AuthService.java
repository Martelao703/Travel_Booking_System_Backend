package com.david.travel_booking_system.service.auth;

import com.david.travel_booking_system.dto.request.auth.LoginRequestDTO;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.request.auth.ResetPasswordRequestDTO;
import com.david.travel_booking_system.dto.response.auth.JwtResponseDTO;
import com.david.travel_booking_system.security.TokenType;
import com.david.travel_booking_system.exception.InvalidTokenException;
import com.david.travel_booking_system.exception.MissingTokenException;
import com.david.travel_booking_system.model.Token;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.security.CustomUserDetails;
import com.david.travel_booking_system.security.jwt.JwtService;
import com.david.travel_booking_system.service.TokenService;
import com.david.travel_booking_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       CustomUserDetailsService userDetailsService, UserService userService,
                       TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public JwtResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        // Delegate User creation to UserService
        User user = userService.createUser(registerRequestDTO);

        // Wrap the User in a CustomUserDetails object
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Issue and persist token pair
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        buildAndSaveToken(user, accessToken, TokenType.ACCESS);
        buildAndSaveToken(user, refreshToken, TokenType.REFRESH);

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
        Integer userId = userDetails.getUser().getId();

        // Revoke all prior tokens
        tokenService.revokeUserTokensByType(userId, TokenType.ACCESS);
        tokenService.revokeUserTokensByType(userId, TokenType.REFRESH);

        // Issue and persist fresh token pair
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        buildAndSaveToken(userDetails.getUser(), accessToken, TokenType.ACCESS);
        buildAndSaveToken(userDetails.getUser(), refreshToken, TokenType.REFRESH);

        // Return both tokens
        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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

        // Validate signature & expiry
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Revoke the old refresh token
        tokenService.revokeTokenByType(refreshToken, TokenType.REFRESH);

        // Issue and persist fresh pair
        String newAccessToken  = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        buildAndSaveToken(userDetails.getUser(), newAccessToken, TokenType.ACCESS);
        buildAndSaveToken(userDetails.getUser(), newRefreshToken, TokenType.REFRESH);

        // Return both tokens
        return JwtResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(HttpServletRequest request) {
        // Extract the raw “Bearer <token>” header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingTokenException("No refresh token provided");
        }

        // Extract the token from the header
        String refreshToken = authHeader.substring(7);

        // revoke that one refresh
        tokenService.revokeTokenByType(refreshToken, TokenType.REFRESH);
    }

    @Transactional
    public void resetOwnPassword(ResetPasswordRequestDTO dto) {
        // Fetch current user from SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Delegate password reset and session revocation to UserService
        userService.resetPasswordAndRevokeSessions(email, dto.getOldPassword(), dto.getNewPassword());
    }

    private void buildAndSaveToken(User user, String rawToken, TokenType type) {
        tokenService.saveToken(
                Token.builder()
                        .user(user)
                        .token(rawToken)
                        .tokenType(type)
                        .expired(false)
                        .revoked(false)
                        .build()
        );
    }
}
