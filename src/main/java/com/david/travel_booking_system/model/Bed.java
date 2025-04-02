package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enumsAndSets.BedType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "bed_type", nullable = false)
    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @DecimalMin(value = "0.0", message = "Length cannot be less than 0")
    private Double length;

    @DecimalMin(value = "0.0", message = "Width cannot be less than 0")
    private Double width;

    @Column(nullable = false)
    @NotNull(message = "Deleted status cannot be null")
    private boolean deleted = false;

    public Bed() {}
}
