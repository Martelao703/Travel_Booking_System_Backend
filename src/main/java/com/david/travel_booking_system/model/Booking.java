package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.validation.annotation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

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
    @Column(nullable = false)
    @NotNull(message = "Booking status cannot be null")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false)
    @NotNull(message = "Paid status cannot be null")
    private boolean paid = false;

    @Column(name = "planned_check_in_date_time", nullable = false)
    @NotNull(message = "Planned Check-in date-time cannot be null")
    @Future(message = "Planned Check-in date-time must be in the future")
    private LocalDateTime plannedCheckInDateTime;

    @Column(name = "planned_check_out_date_time", nullable = false)
    @NotNull(message = "Planned Check-out date-time cannot be null")
    @Future(message = "Planned Check-out date-time must be in the future")
    private LocalDateTime plannedCheckOutDateTime;

    private LocalDateTime actualCheckInDateTime;
    private LocalDateTime actualCheckOutDateTime;

    @Column(name = "number_of_guests", nullable = false)
    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", message = "Total price must be greater than 0")
    private double totalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @NotNull(message = "Deleted status cannot be null")
    private boolean deleted = false;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}