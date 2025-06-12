package com.david.travel_booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"roomType", "bookings"}) // Prevent recursion
public class Room {

    @Id
    @SequenceGenerator(name = "room_id_sequence", sequenceName = "room_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

    @Column(name = "floor_number", nullable = false)
    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @Column(nullable = false)
    @NotNull(message = "Active status cannot be null")
    private boolean active;

    @Column(nullable = false)
    @NotNull(message = "Availability status cannot be null")
    private boolean occupied = false;

    @Column(nullable = false)
    @NotNull(message = "Cleanliness status cannot be null")
    private boolean cleaned;

    @Column(name = "under_maintenance", nullable = false)
    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;

    @Column(nullable = false)
    @NotNull(message = "Deleted status cannot be null")
    private boolean deleted = false;

    @OneToMany(mappedBy = "room")
    @Fetch(FetchMode.SUBSELECT)
    private List<Booking> bookings;

    public Room() {}

    public Room(RoomType roomType, Integer floorNumber, boolean cleaned, boolean underMaintenance) {
        this.roomType = roomType;
        this.floorNumber = floorNumber;
        this.cleaned = cleaned;
        this.underMaintenance = underMaintenance;
        this.bookings = new ArrayList<>();
    }
}
