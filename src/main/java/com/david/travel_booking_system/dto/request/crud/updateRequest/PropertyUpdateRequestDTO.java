package com.david.travel_booking_system.dto.request.crud.updateRequest;

import com.david.travel_booking_system.enumsAndSets.PropertyType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class PropertyUpdateRequestDTO {
    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Active status cannot be null")
    private boolean active;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;

    @NotNull(message = "Latitude cannot be null")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    private Double longitude;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Number of stars cannot be less than 0")
    @Max(value = 5, message = "Number of stars cannot exceed 5")
    private Integer stars;

    @DecimalMin(value = "0.0", message = "User rating cannot be less than 0")
    @DecimalMax(value = "5.0", message = "User rating cannot exceed 5")
    private Double userRating;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> amenities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> nearbyServices;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> houseRules;
}
