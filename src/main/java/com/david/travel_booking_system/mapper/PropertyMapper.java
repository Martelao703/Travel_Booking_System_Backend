package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.PropertyBasicDTO;
import com.david.travel_booking_system.dto.detail.PropertyDetailDTO;
import com.david.travel_booking_system.dto.full.PropertyFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.util.Coordinates;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class})
public interface PropertyMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    PropertyBasicDTO toBasicDTO(Property property);
    List<PropertyBasicDTO> toBasicDTOs(List<Property> properties);

    // Map to DetailDTO
    @Mapping(target = "latitude", source = "coordinates.latitude")
    @Mapping(target = "longitude", source = "coordinates.longitude")
    PropertyDetailDTO toDetailDTO(Property property);
    List<PropertyDetailDTO> toDetailDTOs(List<Property> properties);

    // Map to FullDTO
    @Mapping(target = "latitude", source = "coordinates.latitude")
    @Mapping(target = "longitude", source = "coordinates.longitude")
    PropertyFullDTO toFullDTO(Property property);
    List<PropertyFullDTO> toFullDTOs(List<Property> properties);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create Property from PropertyCreateRequestDTO
    @Mapping(target = "coordinates", expression = "java(mapCoordinates(dto.getLatitude(), dto.getLongitude()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userRating", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Property createPropertyFromDTO(PropertyCreateRequestDTO dto);

    // Update Property from PropertyUpdateRequestDTO
    @Mapping(target = "coordinates", expression = "java(mapCoordinates(inputDTO.getLatitude(), inputDTO.getLongitude()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    void updatePropertyFromDTO(@MappingTarget Property property, PropertyUpdateRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/

    @AfterMapping
    default void initializeFields(@MappingTarget Property property) {
        property.setRoomTypes(new ArrayList<>());
    }

    default Coordinates mapCoordinates(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }
}
