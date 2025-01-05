package com.david.travel_booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Room {
    @Id
    @SequenceGenerator(name = "room_id_sequence", sequenceName = "room_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

    @Column(nullable = false)
    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @NotNull(message = "Active status cannot be null")
    private boolean active = true;

    @Column(nullable = false)
    @NotNull(message = "Availability status cannot be null")
    private boolean available;

    @Column(nullable = false)
    @NotNull(message = "Cleanliness status cannot be null")
    private boolean cleaned;

    @Column(nullable = false)
    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Room() {}

    public Room(RoomType roomType, Integer floorNumber, boolean available, boolean cleaned, boolean underMaintenance) {
        this.roomType = roomType;
        this.floorNumber = floorNumber;
        this.available = available;
        this.cleaned = cleaned;
        this.underMaintenance = underMaintenance;
        this.bookings = new ArrayList<>();
    }
}
