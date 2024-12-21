package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PropertyBasicDTO {
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

    public static PropertyBasicDTO from(Property property) {
        return new PropertyBasicDTO(
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
    }

    public static List<PropertyBasicDTO> from (List<Property> properties) {
        return properties.stream().map(PropertyBasicDTO::from).collect(Collectors.toList());
    }
}
