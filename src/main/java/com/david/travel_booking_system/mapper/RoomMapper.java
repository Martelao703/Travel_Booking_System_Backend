package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.full.RoomFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface RoomMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Mapping to BasicDTO
    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomBasicDTO toBasicDTO(Room room);
    List<RoomBasicDTO> toBasicDTOs(List<Room> rooms);

    // Mapping to FullDTO
    @Mapping(target = "roomTypeId", source = "roomType.id")
    RoomFullDTO toFullDTO(Room room);
    List<RoomFullDTO> toFullDTOs(List<Room> rooms);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Mapping from CreateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType.id", source = "roomTypeId")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Room fromCreateRequestDTO(RoomCreateRequestDTO dto);

    // Mapping from UpdateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Room fromRequestDTO(RoomUpdateRequestDTO dto);
}
