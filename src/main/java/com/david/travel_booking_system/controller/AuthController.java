package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.request.auth.LoginRequestDTO;
import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.response.auth.JwtResponseDTO;
import com.david.travel_booking_system.service.AuthService;
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
    public ResponseEntity<JwtResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        JwtResponseDTO response = new JwtResponseDTO(authService.register(registerRequestDTO));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponseDTO response = new JwtResponseDTO(authService.authenticate(loginRequestDTO));
        return ResponseEntity.ok(response);
    }
}
