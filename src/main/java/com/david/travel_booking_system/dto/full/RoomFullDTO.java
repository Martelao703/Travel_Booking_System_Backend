package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import com.david.travel_booking_system.model.Room;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoomFullDTO {
    private Integer id;
    private Integer roomTypeId;
    private Integer floorNumber;
    private boolean isActive;
    private boolean isAvailable;
    private boolean isCleaned;
    private boolean isUnderMaintenance;

    private List<BookingBasicDTO> bookings;

    public RoomFullDTO(Integer id, Integer roomTypeId, Integer floorNumber, boolean isActive, boolean isAvailable,
                       boolean isCleaned, boolean isUnderMaintenance) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.floorNumber = floorNumber;
        this.isActive = isActive;
        this.isAvailable = isAvailable;
        this.isCleaned = isCleaned;
        this.isUnderMaintenance = isUnderMaintenance;
    }

    public static RoomFullDTO from(Room room) {
        RoomFullDTO roomFullDTO = new RoomFullDTO(
                room.getId(),
                room.getRoomType().getId(),
                room.getFloorNumber(),
                room.isActive(),
                room.isAvailable(),
                room.isCleaned(),
                room.isUnderMaintenance()
        );
        roomFullDTO.setBookings(BookingBasicDTO.from(room.getBookings()));

        return roomFullDTO;
    }

    public static List<RoomFullDTO> from(List<Room> rooms) {
        return rooms.stream().map(RoomFullDTO::from).collect(Collectors.toList());
    }
}
