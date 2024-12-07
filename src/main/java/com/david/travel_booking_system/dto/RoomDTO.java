package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomDTO {
    @NotNull(message = "Room ID cannot be null")
    private Integer id;

    @NotNull(message = "Room type ID cannot be null")
    private Integer roomTypeId;

    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive = true;

    @NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean isCleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<BookingDTO> bookings;

    public RoomDTO(Integer id, Integer roomTypeId, Integer floorNumber, boolean isActive, boolean isAvailable,
                   boolean isCleaned, boolean isUnderMaintenance) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.floorNumber = floorNumber;
        this.isActive = isActive;
        this.isAvailable = isAvailable;
        this.isCleaned = isCleaned;
        this.isUnderMaintenance = isUnderMaintenance;
    }

    public static RoomDTO from(Room room) {
        RoomDTO roomDTO = new RoomDTO(
                room.getId(),
                room.getRoomType().getId(),
                room.getFloorNumber(),
                room.isActive(),
                room.isAvailable(),
                room.isCleaned(),
                room.isUnderMaintenance()
        );
        roomDTO.setBookings(BookingDTO.from(room.getBookings()));

        return roomDTO;
    }

    public static List<RoomDTO> from(List<Room> rooms) {
        return rooms.stream().map(RoomDTO::from).collect(Collectors.toList());
    }
}
