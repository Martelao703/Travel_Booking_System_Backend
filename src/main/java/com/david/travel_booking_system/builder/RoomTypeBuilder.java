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
    private final List<String> roomFacilities = new ArrayList<>();
    private final List<String> bathroomFacilities = new ArrayList<>();
    private final List<String> kitchenFacilities = new ArrayList<>();
    private final List<String> roomRules = new ArrayList<>();
    private final List<Bed> beds = new ArrayList<>();

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

    public RoomTypeBuilder addRoomFacility(String roomFacility) {
        this.roomFacilities.add(roomFacility);
        return this;
    }

    public RoomTypeBuilder addBathroomFacility(String bathroomFacility) {
        this.bathroomFacilities.add(bathroomFacility);
        return this;
    }

    public RoomTypeBuilder addKitchenFacility(String kitchenFacility) {
        this.kitchenFacilities.add(kitchenFacility);
        return this;
    }

    public RoomTypeBuilder addRoomRule(String roomRule) {
        this.roomRules.add(roomRule);
        return this;
    }

    public RoomTypeBuilder addBed(Bed bed) {
        this.beds.add(bed);
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

        // Shallow copy for lists
        roomType.setRoomFacilities(new ArrayList<>(this.roomFacilities));
        roomType.setBathroomFacilities(new ArrayList<>(this.bathroomFacilities));
        roomType.setKitchenFacilities(new ArrayList<>(this.kitchenFacilities));
        roomType.setRoomRules(new ArrayList<>(this.roomRules));
        roomType.setBeds(new ArrayList<>(this.beds));

        return roomType;
    }
}
