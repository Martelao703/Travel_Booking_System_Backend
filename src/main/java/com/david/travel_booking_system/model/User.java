package com.david.travel_booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    private Integer id;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @NotNull(message = "Active status cannot be null")
    private boolean active = true;

    @Column(nullable = false, length = 50)
    @NotNull(message = "First name cannot be null")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Last name cannot be null")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Column(length = 15, unique = true)
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    private String address;

    @Column(columnDefinition = "DATE CHECK (date_of_birth < CURRENT_DATE)")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public User() {
    }
}
