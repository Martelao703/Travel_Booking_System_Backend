package com.david.travel_booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    private boolean isActive = true;

    @Column(nullable = false)
    @NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;

    @Column(nullable = false)
    @NotNull(message = "Cleanliness status cannot be null")
    private boolean isCleaned;

    @Column(nullable = false)
    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Room() {}

    public Room(RoomType roomType, Integer floorNumber, boolean isAvailable, boolean isCleaned, boolean isUnderMaintenance) {
        this.roomType = roomType;
        this.floorNumber = floorNumber;
        this.isAvailable = isAvailable;
        this.isCleaned = isCleaned;
        this.isUnderMaintenance = isUnderMaintenance;
    }
}
