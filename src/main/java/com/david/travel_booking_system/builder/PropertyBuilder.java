package com.david.travel_booking_system.builder;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.util.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class PropertyBuilder {
    private String name;
    private String city;
    private String address;
    private Coordinates coordinates;
    private String description;
    private Integer stars;
    private List<String> amenities = new ArrayList<>();
    private List<String> nearbyServices = new ArrayList<>();
    private List<String> houseRules = new ArrayList<>();

    public PropertyBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PropertyBuilder city(String city) {
        this.city = city;
        return this;
    }

    public PropertyBuilder address(String address) {
        this.address = address;
        return this;
    }

    public PropertyBuilder coordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public PropertyBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PropertyBuilder stars(Integer stars) {
        this.stars = stars;
        return this;
    }

    public PropertyBuilder amenities(List<String> amenities) {
        this.amenities = new ArrayList<>(amenities);
        return this;
    }

    public PropertyBuilder nearbyServices(List<String> nearbyServices) {
        this.nearbyServices = new ArrayList<>(nearbyServices);
        return this;
    }

    public PropertyBuilder houseRules(List<String> houseRules) {
        this.houseRules = new ArrayList<>(houseRules);
        return this;
    }

    public Property build() {
        Property property = new Property();
        property.setName(this.name);
        property.setCity(this.city);
        property.setAddress(this.address);
        property.setCoordinates(this.coordinates);
        property.setDescription(this.description);
        property.setStars(this.stars);
        property.setAmenities(new ArrayList<>(this.amenities));
        property.setNearbyServices(new ArrayList<>(this.nearbyServices));
        property.setHouseRules(new ArrayList<>(this.houseRules));
        property.setRoomTypes(new ArrayList<>());

        return property;
    }
}