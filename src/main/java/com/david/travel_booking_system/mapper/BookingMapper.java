package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "roomId", source = "room.id")
    BookingBasicDTO toBasicDTO(Booking booking);
    List<BookingBasicDTO> toBasicDTOs(List<Booking> bookings);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create Booking from BookingCreateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "room.id", source = "roomId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paid", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "actualCheckInDateTime", ignore = true)
    @Mapping(target = "actualCheckOutDateTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Booking createBookingFromDTO(BookingCreateRequestDTO dto);

    // Update Booking from BookingUpdateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paid", ignore = true)
    @Mapping(target = "plannedCheckInDateTime", ignore = true)
    @Mapping(target = "plannedCheckOutDateTime", ignore = true)
    @Mapping(target = "actualCheckInDateTime", ignore = true)
    @Mapping(target = "actualCheckOutDateTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateBookingFromDTO(@MappingTarget Booking booking, BookingUpdateRequestDTO inputDTO);
}
