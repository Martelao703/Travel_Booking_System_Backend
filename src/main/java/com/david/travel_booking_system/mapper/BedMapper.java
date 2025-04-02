package com.david.travel_booking_system.mapper;

import com.david.travel_booking_system.dto.request.crud.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BedUpdateRequestDTO;
import com.david.travel_booking_system.dto.response.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.response.full.BedFullDTO;
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
    @Mapping(target = "roomTypeId", source = "roomType.id")
    BedBasicDTO toBasicDTO(Bed bed);
    List<BedBasicDTO> toBasicDTOs(List<Bed> beds);

    // Map to FullDTO
    @Mapping(target = "roomTypeId", source = "roomType.id")
    BedFullDTO toFullDTO(Bed bed);
    List<BedFullDTO> toFullDTOs(List<Bed> beds);

    /* from DTO to Entity -------------------------------------------------------------------------------------------*/

    // Create Bed from BedRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType.id", source = "roomTypeId")
    @Mapping(target = "deleted", ignore = true)
    Bed createBedFromDTO(BedCreateRequestDTO dto);

    // Update Bed from BedRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBedFromDTO(@MappingTarget Bed bed, BedUpdateRequestDTO inputDTO);

    /* Helper methods -----------------------------------------------------------------------------------------------*/
}
