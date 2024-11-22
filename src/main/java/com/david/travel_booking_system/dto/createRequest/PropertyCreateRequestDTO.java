package com.david.travel_booking_system.dto.createRequest;

import com.david.travel_booking_system.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PropertyCreateRequestDTO {
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

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> amenities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> nearbyServices;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> houseRules;
}
