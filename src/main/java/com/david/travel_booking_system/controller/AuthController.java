package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.request.auth.LoginRequestDTO;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.response.auth.JwtResponseDTO;
import com.david.travel_booking_system.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody RegisterRequestDTO registerDTO) {
        JwtResponseDTO response = authService.register(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginDTO) {
        JwtResponseDTO response = authService.authenticate(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseDTO> refreshToken(HttpServletRequest request) {
        JwtResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
