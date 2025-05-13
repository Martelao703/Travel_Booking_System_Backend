package com.david.travel_booking_system.dto.response.basic;

import com.david.travel_booking_system.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class UserBasicDTO {
    private Integer id;
    private List<UserRole> roles;
    private boolean active;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
}