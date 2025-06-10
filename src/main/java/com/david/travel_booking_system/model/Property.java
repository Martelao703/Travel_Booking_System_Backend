package com.david.travel_booking_system.model;

import com.david.travel_booking_system.enumsAndSets.PropertyType;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = "roomTypes") // Prevent recursion
public class Property {
    @Id
    @SequenceGenerator(name = "property_id_sequence", sequenceName = "property_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_id_sequence")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Owner cannot be null")
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "property_type")
    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "City cannot be blank")
    private String city;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Column(nullable = false)
    @NotNull(message = "Active status cannot be null")
    private boolean active = true;

    @Column(nullable = false, name = "under_maintenance")
    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;

    @Embedded
    @Column(nullable = false)
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @Column(name = "user_rating")
    @DecimalMin(value = "0.0", message = "User rating cannot be less than 0")
    @DecimalMax(value = "5.0", message = "User rating cannot exceed 5")
    private Double userRating = null;

    @Column(nullable = false)
    @NotNull(message = "Deleted status cannot be null")
    private boolean deleted = false;

    @ElementCollection
    private List<String> amenities;

    @ElementCollection
    @Column(name = "nearby_services")
    private List<String> nearbyServices;

    @ElementCollection
    @Column(name = "house_rules")
    private List<String> houseRules;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "room_types")
    private List<RoomType> roomTypes;

    public Property() {}
}