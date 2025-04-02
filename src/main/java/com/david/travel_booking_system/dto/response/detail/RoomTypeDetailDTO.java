package com.david.travel_booking_system.dto.response.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id", "propertyId", "name", "pricePerNight", "size", "maxCapacity",
        "hasPrivateBathroom", "hasPrivateKitchen", "description", "view",
        "roomFacilities", "bathroomFacilities", "kitchenFacilities", "roomRules"
})
public class RoomTypeDetailDTO {
    private Integer id;
    private Integer propertyId;
    private String name;
    private double pricePerNight;
    private double size;
    private int maxCapacity;

    @Getter(AccessLevel.NONE)
    @JsonProperty("hasPrivateBathroom")
    private boolean hasPrivateBathroom;

    @Getter(AccessLevel.NONE)
    @JsonProperty("hasPrivateKitchen")
    private boolean hasPrivateKitchen;

    private String description;
    private String view;

    private List<String> roomFacilities;
    private List<String> bathroomFacilities;
    private List<String> kitchenFacilities;
    private List<String> roomRules;

    public RoomTypeDetailDTO(Integer id, Integer propertyId, String name, double pricePerNight, double size,
                             int maxCapacity, boolean hasPrivateBathroom, boolean hasPrivateKitchen, String description,
                             String view) {
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
