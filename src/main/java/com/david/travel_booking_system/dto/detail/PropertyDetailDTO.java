package com.david.travel_booking_system.dto.detail;

import com.david.travel_booking_system.enums.PropertyType;
import com.david.travel_booking_system.model.Property;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PropertyDetailDTO {
    private Integer id;
    private PropertyType propertyType;
    private String name;
    private String city;
    private String address;
    private boolean isActive;
    private boolean isUnderMaintenance;
    private Double latitude;
    private Double longitude;
    private String description;
    private Integer stars;
    private Double userRating;

    private List<String> amenities;
    private List<String> nearbyServices;
    private List<String> houseRules;

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

        return propertyDetailDTO;
    }

    public static List<PropertyDetailDTO> from(List<Property> properties) {
        return properties.stream().map(PropertyDetailDTO::from).collect(Collectors.toList());
    }
}
