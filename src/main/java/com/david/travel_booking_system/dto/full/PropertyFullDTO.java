package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.enums.PropertyType;
import com.david.travel_booking_system.model.Property;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PropertyFullDTO {
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

    private List<RoomTypeBasicDTO> roomTypes;

    public PropertyFullDTO(Integer id, PropertyType propertyType, String name, String city, String address,
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

    public static PropertyFullDTO from(Property property) {
        PropertyFullDTO propertyFullDTO = new PropertyFullDTO(
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
        propertyFullDTO.setAmenities(new ArrayList<>(property.getAmenities()));
        propertyFullDTO.setNearbyServices(new ArrayList<>(property.getNearbyServices()));
        propertyFullDTO.setHouseRules(new ArrayList<>(property.getHouseRules()));

        propertyFullDTO.setRoomTypes(RoomTypeBasicDTO.from(property.getRoomTypes()));

        return propertyFullDTO;
    }

    public static List<PropertyFullDTO> from(List<Property> properties) {
        return properties.stream().map(PropertyFullDTO::from).collect(Collectors.toList());
    }
}
