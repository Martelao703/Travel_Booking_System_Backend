package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.basic.RoomBasicDTO;
import com.david.travel_booking_system.model.RoomType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomTypeFullDTO {
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

    public static RoomTypeFullDTO from(RoomType roomType) {
        RoomTypeFullDTO roomTypeFullDTO = new RoomTypeFullDTO(
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
        roomTypeFullDTO.setRoomFacilities(new ArrayList<>(roomType.getRoomFacilities()));
        roomTypeFullDTO.setBathroomFacilities(new ArrayList<>(roomType.getBathroomFacilities()));
        roomTypeFullDTO.setKitchenFacilities(new ArrayList<>(roomType.getKitchenFacilities()));
        roomTypeFullDTO.setRoomRules(new ArrayList<>(roomType.getRoomRules()));

        roomTypeFullDTO.setRooms(RoomBasicDTO.from(roomType.getRooms()));
        roomTypeFullDTO.setBeds(BedBasicDTO.from(roomType.getBeds()));

        return roomTypeFullDTO;
    }

    public static List<RoomTypeFullDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeFullDTO::from).collect(Collectors.toList());
    }
}
