package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.response.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.response.full.RoomFullDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.model.Room;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface RoomMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomBasicDTO toBasicDTO(Room room);
    List<RoomBasicDTO> toBasicDTOs(List<Room> rooms);

    // Map to FullDTO
    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomFullDTO toFullDTO(Room room);
    List<RoomFullDTO> toFullDTOs(List<Room> rooms);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create Room from RoomCreateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType.id", source = "roomTypeId")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "occupied", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Room createRoomFromDTO(RoomCreateRequestDTO dto);

    // Create Room from RoomUpdateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "occupied", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateRoomFromDTO(@MappingTarget Room room, RoomUpdateRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/

    @AfterMapping
    default void afterCreateMapping(@MappingTarget Room room, RoomCreateRequestDTO dto) {
        room.setBookings(new ArrayList<>());
    }
}
