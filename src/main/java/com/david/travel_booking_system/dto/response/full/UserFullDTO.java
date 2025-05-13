package com.david.travel_booking_system.dto.response.full;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.response.basic.PropertyBasicDTO;
import com.david.travel_booking_system.security.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserFullDTO {
    private Integer id;
    private List<UserRole> roles;
    private boolean active;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    private List<PropertyBasicDTO> properties;
    private List<BookingBasicDTO> bookings;

    public UserFullDTO(Integer id, List<UserRole> roles, boolean active, String firstName, String lastName, String email,
                       String phoneNumber, String address, LocalDate dateOfBirth) {
        this.id = id;
        this.roles = roles;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
