package com.david.travel_booking_system.dto.request.specialized;

import com.david.travel_booking_system.security.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserRoleChangeRequestDTO {

    @NotNull(message = "Roles cannot be null")
    @NotEmpty(message = "At least one role must be specified")
    private Set<UserRole> roles;
}
