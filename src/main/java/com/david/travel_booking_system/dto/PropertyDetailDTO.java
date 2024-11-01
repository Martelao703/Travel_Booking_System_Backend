package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.model.enums.PropertyType;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Double userRating;

    private List<String> amenities = new ArrayList<>();

    private List<String> nearbyServices = new ArrayList<>();

    private List<String> houseRules = new ArrayList<>();

    private List<RoomTypeDTO> roomTypes = new ArrayList<>();

    public PropertyDetailDTO(Integer id, PropertyType propertyType, String name, String city, String address,
                             Coordinates coordinates, String description, Integer stars, Double userRating,
                             List<String> amenities, List<String> nearbyServices, List<String> houseRules,
                             List<RoomTypeDTO> roomTypes) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.coordinates = coordinates;
        this.description = description;
        this.stars = stars;
        this.userRating = userRating;
        this.amenities = amenities;
        this.nearbyServices = nearbyServices;
        this.houseRules = houseRules;
        this.roomTypes = roomTypes;
    }

    /*public PropertyDetailDTO(Integer id, PropertyType propertyType, String name, String city, String address,
                             Coordinates coordinates, String description, int stars, double userRating, List<String> amenities,
                             List<String> nearbyServices, List<String> houseRules, List<RoomTypeDTO> roomTypes) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.coordinates = coordinates;
        this.description = description;
        this.stars = stars;
        this.userRating = userRating;
        this.amenities = amenities != null ? amenities : new ArrayList<>();
        this.nearbyServices = nearbyServices != null ? nearbyServices : new ArrayList<>();
        this.houseRules = houseRules != null ? houseRules : new ArrayList<>();
        this.roomTypes = roomTypes != null ? roomTypes : new ArrayList<>();
    }*/

    /*public PropertyDTO from(Property property) {
        return new PropertyDTO(
                property.getId(),
                property.getPropertyType(),
                property.getName(),
                property.getCity(),
                property.getAddress(),
                property.getCoordinates(),
                property.getDescription(),
                property.getStars(),
                property.getUserRating(),
                property.getAmenities(),
                property.getNearbyServices(),
                property.getHouseRules(),
                RoomTypeDTO.from(property.getRoomTypes())
        );
    }*/
}
