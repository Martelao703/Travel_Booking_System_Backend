package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.response.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.response.detail.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.response.full.RoomTypeFullDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.model.RoomType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, BedMapper.class})
public interface RoomTypeMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeBasicDTO toBasicDTO(RoomType roomType);
    List<RoomTypeBasicDTO> toBasicDTOs(List<RoomType> roomTypes);

    // Map to DetailDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeDetailDTO toDetailDTO(RoomType roomType);
    List<RoomTypeDetailDTO> toDetailDTOs(List<RoomType> roomTypes);

    // Map to FullDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeFullDTO toFullDTO(RoomType roomType);
    List<RoomTypeFullDTO> toFullDTOs(List<RoomType> roomTypes);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create RoomType from RoomTypeCreateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property.id", source = "propertyId")
    @Mapping(target = "hasPrivateBathroom", expression = "java(dto.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(dto.hasPrivateKitchen())")
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "beds", ignore = true)
    RoomType createRoomTypeFromDTO(RoomTypeCreateRequestDTO dto);

    // Update RoomType from RoomTypeUpdateRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "hasPrivateBathroom", expression = "java(inputDTO.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(inputDTO.hasPrivateKitchen())")
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "beds", ignore = true)
    void updateRoomTypeFromDTO(@MappingTarget RoomType roomType, RoomTypeUpdateRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/

    @AfterMapping
    default void afterCreateMapping(@MappingTarget RoomType roomType, RoomTypeCreateRequestDTO dto) {
        roomType.setRooms(new ArrayList<>());
        roomType.setBeds(new ArrayList<>());
    }
}
