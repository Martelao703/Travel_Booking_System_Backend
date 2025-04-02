package com.david.travel_booking_system.dto.response.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@JsonPropertyOrder({
        "id", "propertyId", "name", "pricePerNight", "size", "maxCapacity",
        "hasPrivateBathroom", "hasPrivateKitchen", "description", "view",
        "roomFacilities", "bathroomFacilities", "kitchenFacilities", "roomRules"
})
public class RoomTypeBasicDTO {
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

    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public boolean hasPrivateKitchen() {
        return hasPrivateKitchen;
    }
}
