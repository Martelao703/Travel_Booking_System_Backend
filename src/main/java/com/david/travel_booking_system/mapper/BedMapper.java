package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.response.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.response.full.BedFullDTO;
import com.david.travel_booking_system.dto.request.general.BedRequestDTO;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.RoomType;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BedMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    BedBasicDTO toBasicDTO(Bed bed);
    List<BedBasicDTO> toBasicDTOs(List<Bed> beds);

    // Map to FullDTO
    @Mapping(target = "roomTypeIds", source = "roomTypes", qualifiedByName = "mapRoomTypesToIds")
    BedFullDTO toFullDTO(Bed bed);
    @Mapping(target = "roomTypeIds", source = "roomTypes", qualifiedByName = "mapRoomTypesToIds")
    List<BedFullDTO> toFullDTOs(List<Bed> beds);

    @Named("mapRoomTypesToIds")
    default List<Integer> mapRoomTypesToIds(List<RoomType> roomTypes) {
        if (roomTypes == null) {
            return null;
        }
        return roomTypes.stream()
                .map(RoomType::getId)  // Get the id from each RoomType
                .collect(Collectors.toList());
    }

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create Bed from BedRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Bed createBedFromDTO(BedRequestDTO dto);

    // Update Bed from BedRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    void updateBedFromDTO(@MappingTarget Bed bed, BedRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/

    @AfterMapping
    default void afterCreateMapping(@MappingTarget Bed bed, BedRequestDTO dto) {
        bed.setRoomTypes(new ArrayList<>());
    }
}
