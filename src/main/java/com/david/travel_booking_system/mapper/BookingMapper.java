package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.request.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Mapping to BasicDTO
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roomId", source = "room.id")
    BookingBasicDTO toBasicDTO(Booking booking);
    List<BookingBasicDTO> toBasicDTOs(List<Booking> bookings);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Mapping from CreateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "room.id", source = "roomId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paid", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Booking fromCreateRequestDTO(BookingCreateRequestDTO dto);

    // Mapping from UpdateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    Booking fromUpdateRequestDTO(BookingUpdateRequestDTO dto);
}
