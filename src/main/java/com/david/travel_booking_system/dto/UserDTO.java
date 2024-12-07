package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;

    @NotNull(message = "First name cannot be null")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    public static UserDTO from(User user) {
        return new UserDTO(
            user.getId(),
            user.isActive(),
            user.getFirstName(),
            user.getLastName()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }
}
