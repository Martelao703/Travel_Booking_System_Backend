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

    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @Min(value = 0, message = "Length cannot be less than 0")
    private double length;

    @Min(value = 0, message = "Width cannot be less than 0")
    private double width;

    public Bed() {}

    public Bed(Integer id, RoomType roomType, BedType bedType) {
        this.id = id;
        this.roomType = roomType;
        this.bedType = bedType;
    }

}
