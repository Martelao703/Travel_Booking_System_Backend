package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RoomBasicDTO {
    private Integer id;
    private Integer roomTypeId;
    private Integer floorNumber;
    private boolean isActive;
    private boolean isAvailable;
    private boolean isCleaned;
    private boolean isUnderMaintenance;

    public static RoomBasicDTO from(Room room) {
        return new RoomBasicDTO(
                room.getId(),
                room.getRoomType().getId(),
                room.getFloorNumber(),
                room.isActive(),
                room.isAvailable(),
                room.isCleaned(),
                room.isUnderMaintenance()
        );
    }

    public static List<RoomBasicDTO> from(List<Room> rooms) {
        return rooms.stream().map(RoomBasicDTO::from).collect(Collectors.toList());
    }
}
