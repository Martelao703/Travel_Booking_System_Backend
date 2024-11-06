package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.RoomType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomTypeDetailDTO {
    @NotNull(message = "Room type ID cannot be null")
    private Integer id;

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

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<BedDTO> beds;

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

    public static RoomTypeDetailDTO from(RoomType roomType) {
        RoomTypeDetailDTO roomTypeDetailDTO = new RoomTypeDetailDTO(
                roomType.getId(),
                roomType.getProperty().getId(),
                roomType.getName(),
                roomType.getPricePerNight(),
                roomType.getSize(),
                roomType.getMaxCapacity(),
                roomType.isHasPrivateBathroom(),
                roomType.isHasPrivateKitchen(),
                roomType.getDescription(),
                roomType.getView()
        );
        roomTypeDetailDTO.setRoomFacilities(new ArrayList<>(roomType.getRoomFacilities()));
        roomTypeDetailDTO.setBathroomFacilities(new ArrayList<>(roomType.getBathroomFacilities()));
        roomTypeDetailDTO.setKitchenFacilities(new ArrayList<>(roomType.getKitchenFacilities()));
        roomTypeDetailDTO.setRoomRules(new ArrayList<>(roomType.getRoomRules()));
        roomTypeDetailDTO.setBeds(BedDTO.from(roomType.getBeds()));

        return roomTypeDetailDTO;
    }

    public static List<RoomTypeDetailDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeDetailDTO::from).collect(Collectors.toList());
    }
}
