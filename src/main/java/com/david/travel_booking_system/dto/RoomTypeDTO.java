package com.david.travel_booking_system.dto;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeDTO {
    private Integer id;
    private Integer propertyId;
    private String name;
    private double pricePerNight;
    private double size;
    private int maxCapacity;
    private String description;
    private List<String> roomFacilities;
    private List<String> roomRules;
    private List<BedDTO> beds;

    public RoomTypeDTO(Integer id, Integer propertyId, String name, double pricePerNight, double size, int maxCapacity,
                       String description, List<String> roomFacilities, List<String> roomRules, List<BedDTO> beds) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.size = size;
        this.maxCapacity = maxCapacity;
        this.description = description;
        this.roomFacilities = roomFacilities;
        this.roomRules = roomRules;
        this.beds = beds;
    }

    /*public static RoomTypeDTO from(RoomType roomType) {
        return new RoomTypeDTO(
                roomType.getId(),
                roomType.getName(),
                roomType.getPricePerNight(),
                roomType.getSize(),
                roomType.getMaxCapacity(),
                roomType.getDescription(),
                roomType.getRoomFacilities(),
                roomType.getRoomRules(),
                BedDTO.from(roomType.getBeds())
        );
    }*/
}
