package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RoomDTO {
    @NotNull(message = "Room ID cannot be null")
    private Integer id;

    @NotNull(message = "Room type ID cannot be null")
    private Integer roomTypeId;

    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean isCleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;

    public static RoomDTO from(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getRoomType().getId(),
                room.getFloorNumber(),
                room.isAvailable(),
                room.isCleaned(),
                room.isUnderMaintenance()
        );
    }

    public static List<RoomDTO> from(List<Room> rooms) {
        return rooms.stream().map(RoomDTO::from).collect(Collectors.toList());
    }
}
