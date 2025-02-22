package com.david.travel_booking_system.dto.response.basic;

import com.david.travel_booking_system.enumsAndSets.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyBasicDTO {
    private Integer id;
    private PropertyType propertyType;
    private String name;
    private String city;
    private String address;
    private boolean active;
    private boolean underMaintenance;
    private Double latitude;
    private Double longitude;
    private String description;
    private Integer stars;
    private Double userRating;
}