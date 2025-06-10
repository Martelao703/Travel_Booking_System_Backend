package com.david.travel_booking_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.*;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"property", "rooms", "beds"}) // Prevent recursion
public class RoomType {
    @Id
    @SequenceGenerator(name = "roomType_id_sequence", sequenceName = "roomType_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomType_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @NotNull(message = "Property cannot be null")
    private Property property;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Column(name = "price_per_night", nullable = false)
    @NotNull(message = "Price per night cannot be null")
    @DecimalMin(value = "0.0", message = "Price per night cannot be less than 0")
    private double pricePerNight;

    @Column(nullable = false)
    @NotNull(message = "Size cannot be null")
    @DecimalMin(value = "0.0", message = "Size cannot be less than 0")
    private double size;

    @Column(name = "max_capacity", nullable = false)
    @NotNull(message = "Max capacity cannot be null")
    @Min(value = 0, message = "Max capacity cannot be less than 0")
    private int maxCapacity;

    @Column(name = "has_private_bathroom", nullable = false)
    @NotNull(message = "Private bathroom availability cannot be null")
    @Getter(AccessLevel.NONE)
    private boolean hasPrivateBathroom;

    @Column(name = "has_private_kitches", nullable = false)
    @NotNull(message = "Private kitchen availability cannot be null")
    @Getter(AccessLevel.NONE)
    private boolean hasPrivateKitchen;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(length = 50)
    @Size(max = 50, message = "View cannot exceed 50 characters")
    private String view;

    @Column(nullable = false)
    @NotNull(message = "Deleted status cannot be null")
    private boolean deleted = false;

    @ElementCollection
    @Column(name = "room_facilities")
    private List<String> roomFacilities;

    @ElementCollection
    @Column(name = "bathroom_facilities")
    private List<String> bathroomFacilities;

    @ElementCollection
    @Column(name = "kitchen_facilities")
    private List<String> kitchenFacilities;

    @ElementCollection
    @Column(name = "room_rules")
    private List<String> roomRules;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;

    public RoomType() {}

    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public boolean hasPrivateKitchen() {
        return hasPrivateKitchen;
    }

    @PreRemove
    private void removeBedAssociations() {
        this.beds.clear();
    }
}
