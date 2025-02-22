package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@ValidDateRange
public class Booking {
    @Id
    @SequenceGenerator(name = "booking_id_sequence", sequenceName = "booking_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_sequence")
    @Column(nullable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    @NotNull(message = "Room cannot be null")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    @NotNull(message = "Booking status cannot be null")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @NotNull(message = "Paid status cannot be null")
    private boolean paid = false;

    @Column(nullable = false, columnDefinition = "DATE CHECK (planned_check_in_date_time > CURRENT_DATE)")
    @NotNull(message = "Planned Check-in date-time cannot be null")
    @Future(message = "Planned Check-in date-time must be in the future")
    private LocalDateTime plannedCheckInDateTime;

    @Column(nullable = false, columnDefinition = "DATE CHECK (planned_check_out_date_time > CURRENT_DATE)")
    @NotNull(message = "Planned Check-out date-time cannot be null")
    @Future(message = "Planned Check-out date-time must be in the future")
    private LocalDateTime plannedCheckOutDateTime;

    private LocalDateTime actualCheckInDateTime;
    private LocalDateTime actualCheckOutDateTime;

    @Column(nullable = false, columnDefinition = "INT CHECK (number_of_guests >= 1)")
    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    @Column(nullable = false, columnDefinition = "DOUBLE CHECK (total_price >= 0)")
    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be greater than 0")
    private double totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Booking() {
    }

    public Booking(User user, Room room, LocalDateTime plannedCheckInDateTime, LocalDateTime plannedCheckOutDateTime, int numberOfGuests,
                   double totalPrice) {
        this.user = user;
        this.room = room;
        this.plannedCheckInDateTime = plannedCheckInDateTime;
        this.plannedCheckOutDateTime = plannedCheckOutDateTime;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }
}