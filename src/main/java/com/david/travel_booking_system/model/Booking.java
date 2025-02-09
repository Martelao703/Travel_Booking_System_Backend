package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@ValidDateRange
public class Booking {
    @Id
    @SequenceGenerator(name = "booking_id_sequence", sequenceName = "booking_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Room cannot be null")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    @NotNull(message = "Booking status cannot be null")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @NotNull(message = "Paid status cannot be null")
    private boolean paid = false;

    @Column(nullable = false, columnDefinition = "DATE CHECK (check_in_date > CURRENT_DATE)")
    @NotNull(message = "Check-in date cannot be null")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @Column(nullable = false, columnDefinition = "DATE CHECK (check_in_date > CURRENT_DATE)")
    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @Column(nullable = false, columnDefinition = "INT CHECK (number_of_guests >= 1)")
    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    @Column(nullable = false, columnDefinition = "DOUBLE CHECK (total_price >= 0)")
    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be greater than 0")
    private double totalPrice;

    public Booking() {
    }

    public Booking(User user, Room room, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests,
                   double totalPrice) {
        this.user = user;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }
}