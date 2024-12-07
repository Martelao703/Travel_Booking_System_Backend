package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enums.BedType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Bed {
    @Id
    @SequenceGenerator(name = "bed_id_sequence", sequenceName = "bed_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bed_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @Column(columnDefinition = "DOUBLE CHECK (length >= 0)")
    @Min(value = 0, message = "Length cannot be less than 0")
    private Double length;

    @Column(columnDefinition = "DOUBLE CHECK (width >= 0)")
    @Min(value = 0, message = "Width cannot be less than 0")
    private Double width;

    public Bed() {}

    public Bed(RoomType roomType, BedType bedType) {
        this.roomType = roomType;
        this.bedType = bedType;
    }

    public Bed(RoomType roomType, BedType bedType, Double length, Double width) {
        this.roomType = roomType;
        this.bedType = bedType;
        this.length = length;
        this.width = width;
    }
}
