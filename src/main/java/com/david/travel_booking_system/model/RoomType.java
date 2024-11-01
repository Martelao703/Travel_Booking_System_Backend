package com.david.travel_booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class RoomType {
    @Id
    @SequenceGenerator(name = "roomType_id_sequence", sequenceName = "roomType_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomType_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @NotNull(message = "Property cannot be null")
    private Property property;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Price per night cannot be null")
    @Min(value = 0, message = "Price per night cannot be less than 0")
    private double pricePerNight;

    @NotNull(message = "Size cannot be null")
    @Min(value = 0, message = "Size cannot be less than 0")
    private double size;

    @NotNull(message = "Max capacity cannot be null")
    @Min(value = 0, message = "Max capacity cannot be less than 0")
    private int maxCapacity;

    @Size(max = 200, message = "Description cannot exceed 1000 characters")
    private String description;

    @ElementCollection
    private List<String> roomFacilities;

    @ElementCollection
    private List<String> roomRules;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;

    public RoomType() {}

    public RoomType(Integer id, Property property, String name, double pricePerNight, double size, int maxCapacity) {
        this.id = id;
        this.property = property;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.size = size;
        this.maxCapacity = maxCapacity;
    }

    public void addRoomFacility(String roomFacility) {
        roomFacilities.add(roomFacility);
    }

    public void removeRoomFacility(String roomFacility) {
        roomFacilities.remove(roomFacility);
    }

    public void addRoomRule(String roomRule) {
        roomRules.add(roomRule);
    }

    public void removeRoomRule(String roomRule) {
        roomRules.remove(roomRule);
    }

    public void addBed(Bed bed) {
        beds.add(bed);
    }

    public void removeBed(Bed bed) {
        beds.remove(bed);
    }
}
