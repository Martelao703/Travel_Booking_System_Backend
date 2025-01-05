package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.PropertyBasicDTO;
import com.david.travel_booking_system.dto.detail.PropertyDetailDTO;
import com.david.travel_booking_system.dto.full.PropertyFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.util.Coordinates;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    // Map from CreateRequestDTO to Entity
    @Mapping(target = "coordinates", expression = "java(mapCoordinates(dto.getLatitude(), dto.getLongitude()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userRating", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Property fromCreateRequestDTO(PropertyCreateRequestDTO dto);

    // Map from UpdateRequestDTO to Entity
    @Mapping(target = "coordinates", expression = "java(mapCoordinates(dto.getLatitude(), dto.getLongitude()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Property fromUpdateRequestDTO(PropertyUpdateRequestDTO dto);

    default Coordinates mapCoordinates(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }
}
