package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.full.BedFullDTO;
import com.david.travel_booking_system.dto.request.BedRequestDTO;
import com.david.travel_booking_system.model.Bed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class})
public interface BedMapper {
    /* from Entity to DTO -------------------------------------------------------------------------------------------*/

    // Mapping to BasicDTO
    BedBasicDTO toBasicDTO(Bed bed);
    List<BedBasicDTO> toBasicDTOs(List<Bed> beds);

    // Mapping to FullDTO
    BedFullDTO toFullDTO(Bed bed);
    List<BedFullDTO> toFullDTOs(List<Bed> beds);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Mapping from RequestDTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Bed fromRequestDTO(BedRequestDTO dto);
}
