package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.response.basic.UserBasicDTO;
import com.david.travel_booking_system.dto.response.full.UserFullDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BookingMapper.class})
public interface UserMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    UserBasicDTO toBasicDTO(User user);
    List<UserBasicDTO> toBasicDTOs(List<User> users);

    // Map to FullDTO
    UserFullDTO toFullDTO(User user);
    List<UserFullDTO> toFullDTOs(List<User> users);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create User from UserCreateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    User createUserFromDTO(UserCreateRequestDTO dto);

    // Update User from UserUpdateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateUserFromDTO(@MappingTarget User user, UserUpdateRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/

    @AfterMapping
    default void afterCreateMapping(@MappingTarget User user, UserCreateRequestDTO dto) {
        user.setBookings(new ArrayList<>());
    }
}
