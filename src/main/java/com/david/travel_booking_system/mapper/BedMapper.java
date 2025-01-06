package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.full.BedFullDTO;
import com.david.travel_booking_system.dto.request.BedRequestDTO;
import com.david.travel_booking_system.model.Bed;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class})
public interface BedMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Map to BasicDTO
    BedBasicDTO toBasicDTO(Bed bed);
    List<BedBasicDTO> toBasicDTOs(List<Bed> beds);

    // Map to FullDTO
    BedFullDTO toFullDTO(Bed bed);
    List<BedFullDTO> toFullDTOs(List<Bed> beds);

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
    default void initializeFields(@MappingTarget Bed bed) {
        bed.setRoomTypes(new ArrayList<>());
    }
}
