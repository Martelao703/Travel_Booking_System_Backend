package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserFullDTO {
    private Integer id;
    private boolean active;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    private List<BookingBasicDTO> bookings;

    public UserFullDTO(Integer id, boolean active, String firstName, String lastName, String email,
                       String phoneNumber, String address, LocalDate dateOfBirth) {
        this.id = id;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
