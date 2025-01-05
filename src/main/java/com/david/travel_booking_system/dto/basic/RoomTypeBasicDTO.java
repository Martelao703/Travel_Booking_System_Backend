package com.david.travel_booking_system.dto.basic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class RoomTypeBasicDTO {
    private Integer id;
    private Integer propertyId;
    private String name;
    private double pricePerNight;
    private double size;
    private int maxCapacity;

    @Getter(AccessLevel.NONE)
    private boolean hasPrivateBathroom;

    @Getter(AccessLevel.NONE)
    private boolean hasPrivateKitchen;

    private String description;
    private String view;

    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public boolean hasPrivateKitchen() {
        return hasPrivateKitchen;
    }
}
