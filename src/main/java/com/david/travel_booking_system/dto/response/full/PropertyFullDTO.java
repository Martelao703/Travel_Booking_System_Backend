package com.david.travel_booking_system.dto.response.full;

import com.david.travel_booking_system.dto.response.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.enumsAndSets.PropertyType;
import lombok.Data;

import java.util.List;

@Data
public class PropertyFullDTO {
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

    private List<String> amenities;
    private List<String> nearbyServices;
    private List<String> houseRules;

    private List<RoomTypeBasicDTO> roomTypes;

    public PropertyFullDTO(Integer id, PropertyType propertyType, String name, String city, String address,
                           boolean active, boolean underMaintenance, Double latitude, Double longitude,
                           String description, Integer stars, Double userRating) {
        this.id = id;
        this.propertyType = propertyType;
        this.name = name;
        this.city = city;
        this.address = address;
        this.active = active;
        this.underMaintenance = underMaintenance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.stars = stars;
        this.userRating = userRating;
    }
}
