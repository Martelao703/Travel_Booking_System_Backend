package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.model.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RoomTypeBasicDTO {
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

    public static RoomTypeBasicDTO from(RoomType roomType) {
        return new RoomTypeBasicDTO(
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
    }

    public static List<RoomTypeBasicDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeBasicDTO::from).collect(Collectors.toList());
    }
}
