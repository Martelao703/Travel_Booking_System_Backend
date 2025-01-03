package com.david.travel_booking_system.dto.detail;

import com.david.travel_booking_system.model.RoomType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomTypeDetailDTO {
    private Integer id;
    private Integer propertyId;
    private String name;
    private double pricePerNight;
    private double size;
    private int maxCapacity;
    private boolean hasPrivateBathroom;
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

        return roomTypeDetailDTO;
    }

    public static List<RoomTypeDetailDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeDetailDTO::from).collect(Collectors.toList());
    }
}
