package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import com.david.travel_booking_system.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserFullDTO {
    private Integer id;
    private boolean isActive;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    private List<BookingBasicDTO> bookings;

    public UserFullDTO(Integer id, boolean isActive, String firstName, String lastName, String email,
                       String phoneNumber, String address, LocalDate dateOfBirth) {
        this.id = id;
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public static UserFullDTO from(User user) {
        UserFullDTO userFullDTO = new UserFullDTO(
            user.getId(),
            user.isActive(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getDateOfBirth()
        );
        userFullDTO.setBookings(BookingBasicDTO.from(user.getBookings()));

        return userFullDTO;
    }

    public static List<UserFullDTO> from(List<User> users) {
        return users.stream().map(UserFullDTO::from).collect(Collectors.toList());
    }
}
