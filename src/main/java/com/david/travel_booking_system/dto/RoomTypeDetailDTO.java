package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.RoomType;
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

    private List<String> roomFacilities;
    private List<String> bathroomFacilities;
    private List<String> kitchenFacilities;
    private List<String> roomRules;
    private List<BedDTO> beds;

    public RoomTypeDetailDTO(Integer id, Integer propertyId, String name, double pricePerNight, double size,
                             int maxCapacity, boolean hasPrivateBathroom, boolean hasPrivateKitchen, String description,
                             String view, List<String> roomFacilities, List<String> bathroomFacilities,
                             List<String> kitchenFacilities, List<String> roomRules, List<BedDTO> beds) {
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

        // Shallow copy for lists
        this.roomFacilities = roomFacilities != null ? new ArrayList<>(roomFacilities) : new ArrayList<>();
        this.bathroomFacilities = bathroomFacilities != null ? new ArrayList<>(bathroomFacilities) : new ArrayList<>();
        this.kitchenFacilities = kitchenFacilities != null ? new ArrayList<>(kitchenFacilities) : new ArrayList<>();
        this.roomRules = roomRules != null ? new ArrayList<>(roomRules) : new ArrayList<>();
        this.beds = beds != null ? new ArrayList<>(beds) : new ArrayList<>();
    }

    public static RoomTypeDetailDTO from(RoomType roomType) {
        return new RoomTypeDetailDTO(
                roomType.getId(),
                roomType.getProperty().getId(),
                roomType.getName(),
                roomType.getPricePerNight(),
                roomType.getSize(),
                roomType.getMaxCapacity(),
                roomType.isHasPrivateBathroom(),
                roomType.isHasPrivateKitchen(),
                roomType.getDescription(),
                roomType.getView(),
                roomType.getRoomFacilities(),
                roomType.getBathroomFacilities(),
                roomType.getKitchenFacilities(),
                roomType.getRoomRules(),
                BedDTO.from(roomType.getBeds())
        );
    }

    public static List<RoomTypeDetailDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeDetailDTO::from).collect(Collectors.toList());
    }
}
