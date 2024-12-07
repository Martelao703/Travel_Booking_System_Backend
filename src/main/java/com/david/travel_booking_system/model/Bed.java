package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enums.BedType;
import jakarta.persistence.*;
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

    @ManyToMany(mappedBy = "beds")
    private List<RoomType> roomTypes;

    public Bed() {}

    public Bed(BedType bedType) {
        this.bedType = bedType;
        this.roomTypes = new ArrayList<>();
    }

    public Bed(BedType bedType, Double length, Double width) {
        this.bedType = bedType;
        this.length = length;
        this.width = width;
        this.roomTypes = new ArrayList<>();
    }
}
