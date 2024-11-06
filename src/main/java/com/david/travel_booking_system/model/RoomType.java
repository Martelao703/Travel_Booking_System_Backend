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

    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Price per night cannot be null")
    @Min(value = 0, message = "Price per night cannot be less than 0")
    private double pricePerNight;

    @Column(nullable = false)
    @NotNull(message = "Size cannot be null")
    @Min(value = 0, message = "Size cannot be less than 0")
    private double size;

    @Column(nullable = false)
    @NotNull(message = "Max capacity cannot be null")
    @Min(value = 0, message = "Max capacity cannot be less than 0")
    private int maxCapacity;

    @Column(nullable = false)
    @NotNull(message = "hasPrivateBathroom cannot be null")
    private boolean hasPrivateBathroom;

    @Column(nullable = false)
    @NotNull(message = "hasPrivateKitchen cannot be null")
    private boolean hasPrivateKitchen;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 50, message = "View cannot exceed 50 characters")
    private String view;

    @ElementCollection
    private List<String> roomFacilities;

    @ElementCollection
    private List<String> bathroomFacilities;

    @ElementCollection
    private List<String> kitchenFacilities;

    @ElementCollection
    private List<String> roomRules;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;

    public RoomType() {}

    public void addRoomFacility(String roomFacility) {
        roomFacilities.add(roomFacility);
    }

    public void removeRoomFacility(String roomFacility) {
        roomFacilities.remove(roomFacility);
    }

    public void addBathroomFacility(String bathroomFacility) {
        bathroomFacilities.add(bathroomFacility);
    }

    public void removeBathroomFacility(String bathroomFacility) {
        bathroomFacilities.remove(bathroomFacility);
    }

    public void addKitchenFacility(String kitchenFacility) {
        kitchenFacilities.add(kitchenFacility);
    }

    public void removeKitchenFacility(String kitchenFacility) {
        kitchenFacilities.remove(kitchenFacility);
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
