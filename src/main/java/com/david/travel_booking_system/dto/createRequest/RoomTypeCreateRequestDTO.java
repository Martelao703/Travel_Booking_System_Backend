package com.david.travel_booking_system.dto.createRequest;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoomTypeCreateRequestDTO {
    @NotNull(message = "Property ID cannot be null")
    private Integer propertyId;

    @NotNull(message = "Name cannot be null")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Price per night cannot be null")
    @Min(value = 0, message = "Price per night cannot be less than 0")
    private double pricePerNight;

    @NotNull(message = "Size cannot be null")
    @Min(value = 0, message = "Size cannot be less than 0")
    private double size;

    @NotNull(message = "Max capacity cannot be null")
    @Min(value = 0, message = "Max capacity cannot be less than 0")
    private int maxCapacity;

    @NotNull(message = "hasPrivateBathroom cannot be null")
    private boolean hasPrivateBathroom;

    @NotNull(message = "hasPrivateKitchen cannot be null")
    private boolean hasPrivateKitchen;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 50, message = "View cannot exceed 50 characters")
    private String view;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> roomFacilities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> bathroomFacilities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> kitchenFacilities;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<String> roomRules;
}
