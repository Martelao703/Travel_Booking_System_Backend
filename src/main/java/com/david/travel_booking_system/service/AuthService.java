package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.auth.LoginRequestDTO;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                       CustomUserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Transactional
    public String register(RegisterRequestDTO registerRequestDTO) {
        // Delegate User creation to UserService
        User user = userService.createUser(registerRequestDTO);

        // Generate and return token
        return jwtProvider.generateToken(user.getEmail());
    }

    public String authenticate(LoginRequestDTO loginRequestDTO) {
        // Create an authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );

        // AuthManager verifies the credentials
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Store the successful authentication in the SecurityContext so subsequent requests can access it
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate and return token if successful
        return jwtProvider.generateToken(loginRequestDTO.getEmail());
    }
}
