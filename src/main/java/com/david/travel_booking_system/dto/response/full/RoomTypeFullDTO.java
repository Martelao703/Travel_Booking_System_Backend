package com.david.travel_booking_system.dto.response.full;

import com.david.travel_booking_system.dto.response.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomBasicDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class RoomTypeFullDTO {
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

    private List<String> roomFacilities;
    private List<String> bathroomFacilities;
    private List<String> kitchenFacilities;
    private List<String> roomRules;

    private List<RoomBasicDTO> rooms;
    private List<BedBasicDTO> beds;

    public RoomTypeFullDTO(Integer id, Integer propertyId, String name, double pricePerNight, double size, int maxCapacity,
                           boolean hasPrivateBathroom, boolean hasPrivateKitchen, String description, String view) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.size = size;
        this.maxCapacity = maxCapacity;
        this.hasPrivateBathroom = hasPrivateBathroom;
        this.hasPrivateKitchen = hasPrivateKitchen;
        this.description = description;
        this.view = view;
    }

    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public boolean hasPrivateKitchen() {
        return hasPrivateKitchen;
    }
}
