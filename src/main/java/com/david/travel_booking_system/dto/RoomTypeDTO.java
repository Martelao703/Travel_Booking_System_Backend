package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.RoomType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomTypeDTO {
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

    @NotNull(message = "Max capacity cannot be null")
    @Min(value = 0, message = "Max capacity cannot be less than 0")
    private int maxCapacity;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<RoomDTO> rooms;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<BedDTO> beds;

    public RoomTypeDTO(Integer id, Integer propertyId, String name, double pricePerNight, int maxCapacity) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.maxCapacity = maxCapacity;
    }

    public static RoomTypeDTO from(RoomType roomType) {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO(
                roomType.getId(),
                roomType.getProperty().getId(),
                roomType.getName(),
                roomType.getPricePerNight(),
                roomType.getMaxCapacity()
        );
        roomTypeDTO.setRooms(RoomDTO.from(roomType.getRooms()));
        roomTypeDTO.setBeds(BedDTO.from(roomType.getBeds()));

        return roomTypeDTO;
    }

    public static List<RoomTypeDTO> from(List<RoomType> roomTypes) {
        return roomTypes.stream().map(RoomTypeDTO::from).collect(Collectors.toList());
    }
}
