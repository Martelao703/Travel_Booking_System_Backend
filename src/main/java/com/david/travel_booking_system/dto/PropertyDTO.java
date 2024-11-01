package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.enums.PropertyType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyDTO {
    @NotNull(message = "Property ID cannot be null")
    private Integer id;

    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    @NotNull(message = "Name cannot be null")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "City cannot be null")
    private String city;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Double userRating;

    public PropertyDTO from(Property property) {
        return new PropertyDTO(
                property.getId(),
                property.getPropertyType(),
                property.getName(),
                property.getCity(),
                property.getDescription(),
                property.getStars(),
                property.getUserRating()
        );
    }
}
