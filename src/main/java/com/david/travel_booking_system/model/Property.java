package com.david.travel_booking_system.model;

import com.david.travel_booking_system.model.enums.PropertyType;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Property {
    @Id
    @SequenceGenerator(name = "property_id_sequence", sequenceName = "property_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_id_sequence")
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Name cannot be null") @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "City cannot be null")
    private String city;

    @Column(nullable = false)
    @NotNull(message = "Address cannot be null")
    private String address;

    @Embedded
    @Column(nullable = false)
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(columnDefinition = "INT CHECK (stars >= 0 AND stars <= 5)")
    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @Column(columnDefinition = "DOUBLE CHECK (userRating >= 0 AND userRating <= 5)")
    @Min(value = 0, message = "User rating cannot be less than 0")
    @Max(value = 5, message = "User rating cannot exceed 5")
    private Double userRating;

    @ElementCollection
    private List<String> amenities;

    @ElementCollection
    private List<String> nearbyServices;

    @ElementCollection
    private List<String> houseRules;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes;

    public Property() {
        this.amenities = new ArrayList<>();
        this.nearbyServices = new ArrayList<>();
        this.houseRules = new ArrayList<>();
        this.roomTypes = new ArrayList<>();
    }

    public Property(Integer id, PropertyType propertyType, String name, String city, String address,
                    Coordinates coordinates) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.coordinates = coordinates;
        this.amenities = new ArrayList<>();
        this.nearbyServices = new ArrayList<>();
        this.houseRules = new ArrayList<>();
        this.roomTypes = new ArrayList<>();
    }

    public void addAmenity(String amenity) {
        amenities.add(amenity);
    }

    public void removeAmenity(String amenity) {
        amenities.remove(amenity);
    }

    public void addNearbyService(String nearbyService) {
        nearbyServices.add(nearbyService);
    }

    public void removeNearbyService(String nearbyService) {
        nearbyServices.remove(nearbyService);
    }

    public void addHouseRule(String houseRule) {
        houseRules.add(houseRule);
    }

    public void removeHouseRule(String houseRule) {
        houseRules.remove(houseRule);
    }

    public void addRoomType(RoomType roomType) {
        roomTypes.add(roomType);
    }

    public void removeRoomType(RoomType roomType) {
        roomTypes.remove(roomType);
    }
}