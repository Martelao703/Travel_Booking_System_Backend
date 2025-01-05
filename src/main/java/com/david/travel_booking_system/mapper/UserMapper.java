package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.UserBasicDTO;
import com.david.travel_booking_system.dto.full.UserFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface UserMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Mapping to BasicDTO
    UserBasicDTO toBasicDTO(User user);
    List<UserBasicDTO> toBasicDTOs(List<User> users);

    // Mapping to FullDTO
    UserFullDTO toFullDTO(User user);
    List<UserFullDTO> toFullDTOs(List<User> users);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Mapping from CreateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    User fromCreateRequestDTO(UserCreateRequestDTO dto);

    // Mapping from UpdateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    User fromUpdateRequestDTO(UserUpdateRequestDTO dto);
}
