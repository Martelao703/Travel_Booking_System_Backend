package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserBasicDTO {
    private Integer id;
    private boolean isActive;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    public static UserBasicDTO from(User user) {
        return new UserBasicDTO(
            user.getId(),
            user.isActive(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getDateOfBirth()
        );
    }

    public static List<UserBasicDTO> from(List<User> users) {
        return users.stream().map(UserBasicDTO::from).collect(Collectors.toList());
    }
}
