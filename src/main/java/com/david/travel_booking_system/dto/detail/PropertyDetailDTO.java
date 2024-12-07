package com.david.travel_booking_system.dto.detail;

import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.enums.PropertyType;
import com.david.travel_booking_system.model.Property;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
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

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;

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

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> amenities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> nearbyServices;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> houseRules;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<RoomTypeDTO> roomTypes;

    public PropertyDetailDTO(Integer id, PropertyType propertyType, String name, String city, String address,
                             boolean isActive, boolean isUnderMaintenance, Double latitude, Double longitude,
                             String description, Integer stars, Double userRating) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.isActive = isActive;
        this.isUnderMaintenance = isUnderMaintenance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.stars = stars;
        this.userRating = userRating;
    }

    public static PropertyDetailDTO from(Property property) {
        PropertyDetailDTO propertyDetailDTO = new PropertyDetailDTO(
                property.getId(),
                property.getPropertyType(),
                property.getName(),
                property.getCity(),
                property.getAddress(),
                property.isActive(),
                property.isUnderMaintenance(),
                property.getCoordinates().getLatitude(),
                property.getCoordinates().getLongitude(),
                property.getDescription(),
                property.getStars(),
                property.getUserRating()
        );
        propertyDetailDTO.setAmenities(new ArrayList<>(property.getAmenities()));
        propertyDetailDTO.setNearbyServices(new ArrayList<>(property.getNearbyServices()));
        propertyDetailDTO.setHouseRules(new ArrayList<>(property.getHouseRules()));
        propertyDetailDTO.setRoomTypes(RoomTypeDTO.from(property.getRoomTypes()));

        return propertyDetailDTO;
    }

    public static List<PropertyDetailDTO> from(List<Property> properties) {
        return properties.stream().map(PropertyDetailDTO::from).collect(Collectors.toList());
    }
}
