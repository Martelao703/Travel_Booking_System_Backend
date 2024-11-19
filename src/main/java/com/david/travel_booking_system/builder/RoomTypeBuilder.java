package com.david.travel_booking_system.builder;

import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeBuilder {
    private Property property;
    private String name;
    private double pricePerNight;
    private double size;
    private int maxCapacity;
    private boolean hasPrivateBathroom;
    private boolean hasPrivateKitchen;
    private String description;
    private String view;
    private List<String> roomFacilities = new ArrayList<>();;
    private List<String> bathroomFacilities = new ArrayList<>();;
    private List<String> kitchenFacilities = new ArrayList<>();;
    private List<String> roomRules = new ArrayList<>();;

    public RoomTypeBuilder property(Property property) {
        this.property = property;
        return this;
    }

    public RoomTypeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public RoomTypeBuilder pricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
        return this;
    }

    public RoomTypeBuilder size(double size) {
        this.size = size;
        return this;
    }

    public RoomTypeBuilder maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public RoomTypeBuilder hasPrivateBathroom(boolean hasPrivateBathroom) {
        this.hasPrivateBathroom = hasPrivateBathroom;
        return this;
    }

    public RoomTypeBuilder hasPrivateKitchen(boolean hasPrivateKitchen) {
        this.hasPrivateKitchen = hasPrivateKitchen;
        return this;
    }

    public RoomTypeBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RoomTypeBuilder view(String view) {
        this.view = view;
        return this;
    }

    public RoomTypeBuilder roomFacilities(List<String> roomFacilities) {
        this.roomFacilities = new ArrayList<>(roomFacilities);
        return this;
    }

    public RoomTypeBuilder bathroomFacilities(List<String> bathroomFacilities) {
        this.bathroomFacilities = new ArrayList<>(bathroomFacilities);
        return this;
    }

    public RoomTypeBuilder kitchenFacilities(List<String> kitchenFacilities) {
        this.kitchenFacilities = new ArrayList<>(kitchenFacilities);
        return this;
    }

    public RoomTypeBuilder roomRules(List<String> roomRules) {
        this.roomRules = new ArrayList<>(roomRules);
        return this;
    }

    public RoomType build() {
        RoomType roomType = new RoomType();
        roomType.setProperty(property);
        roomType.setName(name);
        roomType.setPricePerNight(pricePerNight);
        roomType.setSize(size);
        roomType.setMaxCapacity(maxCapacity);
        roomType.setHasPrivateBathroom(hasPrivateBathroom);
        roomType.setHasPrivateKitchen(hasPrivateKitchen);
        roomType.setDescription(description);
        roomType.setView(view);
        roomType.setRoomFacilities(new ArrayList<>(roomFacilities));
        roomType.setBathroomFacilities(new ArrayList<>(bathroomFacilities));
        roomType.setKitchenFacilities(new ArrayList<>(kitchenFacilities));
        roomType.setRoomRules(new ArrayList<>(roomRules));
        roomType.setRooms(new ArrayList<>());
        roomType.setBeds(new ArrayList<>());

        return roomType;
    }
}
