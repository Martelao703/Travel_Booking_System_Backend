package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.detail.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.full.RoomTypeFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.model.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, BedMapper.class})
public interface RoomTypeMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Mapping to BasicDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeBasicDTO toBasicDTO(RoomType roomType);
    List<RoomTypeBasicDTO> toBasicDTOs(List<RoomType> roomTypes);

    // Mapping to DetailDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeDetailDTO toDetailDTO(RoomType roomType);
    List<RoomTypeDetailDTO> toDetailDTOs(List<RoomType> roomTypes);

    // Mapping to FullDTO
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "hasPrivateBathroom", expression = "java(roomType.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(roomType.hasPrivateKitchen())")
    RoomTypeFullDTO toFullDTO(RoomType roomType);
    List<RoomTypeFullDTO> toFullDTOs(List<RoomType> roomTypes);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Mapping from CreateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property.id", source = "propertyId")
    @Mapping(target = "hasPrivateBathroom", expression = "java(dto.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(dto.hasPrivateKitchen())")
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "beds", ignore = true)
    RoomType fromCreateRequestDTO(RoomTypeCreateRequestDTO dto);

    // Mapping from UpdateRequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "hasPrivateBathroom", expression = "java(dto.hasPrivateBathroom())")
    @Mapping(target = "hasPrivateKitchen", expression = "java(dto.hasPrivateKitchen())")
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "beds", ignore = true)
    RoomType fromRequestDTO(RoomTypeUpdateRequestDTO dto);
}
