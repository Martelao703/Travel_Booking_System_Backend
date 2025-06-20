package com.david.travel_booking_system.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Old Password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New Password cannot be blank")
    private String newPassword;
}
