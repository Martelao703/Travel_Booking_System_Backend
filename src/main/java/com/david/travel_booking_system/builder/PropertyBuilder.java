package com.david.travel_booking_system.builder;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
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
    private Double userRating;
    private final List<String> amenities = new ArrayList<>();
    private final List<String> nearbyServices = new ArrayList<>();
    private final List<String> houseRules = new ArrayList<>();
    private final List<RoomType> roomTypes = new ArrayList<>();

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

    public PropertyBuilder userRating(Double userRating) {
        this.userRating = userRating;
        return this;
    }

    public PropertyBuilder addAmenity(String amenity) {
        this.amenities.add(amenity);
        return this;
    }


    public PropertyBuilder addNearbyService(String nearbyService) {
        this.nearbyServices.add(nearbyService);
        return this;
    }

    public PropertyBuilder addHouseRule(String houseRule) {
        this.houseRules.add(houseRule);
        return this;
    }

    public PropertyBuilder addRoomType(RoomType roomType) {
        this.roomTypes.add(roomType);
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
        property.setUserRating(this.userRating);

        // Shallow copy for lists
        property.setAmenities(new ArrayList<>(this.amenities));
        property.setNearbyServices(new ArrayList<>(this.nearbyServices));
        property.setHouseRules(new ArrayList<>(this.houseRules));
        property.setRoomTypes(new ArrayList<>(this.roomTypes));

        return property;
    }
}