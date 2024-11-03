package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.enums.PropertyType;
import com.david.travel_booking_system.model.Property;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PropertyDetailDTO {
    @NotNull(message = "Property ID cannot be null")
    private Integer id;

    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    @NotNull(message = "Name cannot be null")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "City cannot be null")
    private String city;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Latitude cannot be null")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    private Double longitude;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Double userRating;

    private List<String> amenities;
    private List<String> nearbyServices;
    private List<String> houseRules;
    private List<RoomTypeDTO> roomTypes;

    public PropertyDetailDTO(Integer id, PropertyType propertyType, String name, String city, String address,
                             Double latitude, Double longitude, String description, Integer stars, Double userRating,
                             List<String> amenities, List<String> nearbyServices, List<String> houseRules,
                             List<RoomTypeDTO> roomTypes) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.stars = stars;
        this.userRating = userRating;

        // Shallow copy for lists
        this.amenities = amenities != null ? new ArrayList<>(amenities) : new ArrayList<>();
        this.nearbyServices = nearbyServices != null ? new ArrayList<>(nearbyServices) : new ArrayList<>();
        this.houseRules = houseRules != null ? new ArrayList<>(houseRules) : new ArrayList<>();
        this.roomTypes = roomTypes != null ? new ArrayList<>(roomTypes) : new ArrayList<>();
    }

    public static PropertyDetailDTO from(Property property) {
        return new PropertyDetailDTO(
                property.getId(),
                property.getPropertyType(),
                property.getName(),
                property.getCity(),
                property.getAddress(),
                property.getCoordinates().getLatitude(),
                property.getCoordinates().getLongitude(),
                property.getDescription(),
                property.getStars(),
                property.getUserRating(),
                property.getAmenities(),
                property.getNearbyServices(),
                property.getHouseRules(),
                RoomTypeDTO.from(property.getRoomTypes())
        );
    }

    public static List<PropertyDetailDTO> from(List<Property> properties) {
        return properties.stream().map(PropertyDetailDTO::from).collect(Collectors.toList());
    }
}
