package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.dto.RoomTypeDetailDTO;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomService roomService;
    private final BedService bedService;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, RoomService roomService , BedService bedService) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomService = roomService;
        this.bedService = bedService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public RoomType createRoomType(@Valid RoomTypeDetailDTO roomTypeDetailDTO, Property property) {
        // Build RoomType object from DTO
        RoomType roomType = new RoomTypeBuilder()
                .property(property)
                .name(roomTypeDetailDTO.getName())
                .pricePerNight(roomTypeDetailDTO.getPricePerNight())
                .size(roomTypeDetailDTO.getSize())
                .maxCapacity(roomTypeDetailDTO.getMaxCapacity())
                .hasPrivateBathroom(roomTypeDetailDTO.isHasPrivateBathroom())
                .hasPrivateKitchen(roomTypeDetailDTO.isHasPrivateKitchen())
                .description(roomTypeDetailDTO.getDescription())
                .view(roomTypeDetailDTO.getView())
                .roomFacilities(roomTypeDetailDTO.getRoomFacilities())
                .bathroomFacilities(roomTypeDetailDTO.getBathroomFacilities())
                .kitchenFacilities(roomTypeDetailDTO.getKitchenFacilities())
                .roomRules(roomTypeDetailDTO.getRoomRules())
                .build();

        // Save RoomType to generate ID and associate it with Beds
        roomType = roomTypeRepository.save(roomType);

        // Delegate Room creation to RoomService
        roomType.setRooms(roomService.createRooms(roomTypeDetailDTO.getRooms(), roomType));

        // Delegate Bed creation to BedService
        roomType.setBeds(bedService.createBeds(roomTypeDetailDTO.getBeds(), roomType));

        return roomType;
    }

    @Transactional
    public List<RoomType> createRoomTypes(List<RoomTypeDetailDTO> roomTypeDetailDTOs, Property property) {
        return roomTypeDetailDTOs.stream()
                .map(roomTypeDetailDTO -> createRoomType(roomTypeDetailDTO, property))
                .collect(Collectors.toList());
    }
}