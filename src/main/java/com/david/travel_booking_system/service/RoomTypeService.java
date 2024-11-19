package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final PropertyService propertyService;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, PropertyService propertyService) {
        this.roomTypeRepository = roomTypeRepository;
        this.propertyService = propertyService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public RoomType createRoomType(@Valid RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        // Find associated Property
        Property property = propertyService.getPropertyById(roomTypeCreateRequestDTO.getPropertyId());

        // Build RoomType object from DTO
        RoomType roomType = new RoomTypeBuilder()
                .property(property)
                .name(roomTypeCreateRequestDTO.getName())
                .pricePerNight(roomTypeCreateRequestDTO.getPricePerNight())
                .size(roomTypeCreateRequestDTO.getSize())
                .maxCapacity(roomTypeCreateRequestDTO.getMaxCapacity())
                .hasPrivateBathroom(roomTypeCreateRequestDTO.isHasPrivateBathroom())
                .hasPrivateKitchen(roomTypeCreateRequestDTO.isHasPrivateKitchen())
                .description(roomTypeCreateRequestDTO.getDescription())
                .view(roomTypeCreateRequestDTO.getView())
                .roomFacilities(roomTypeCreateRequestDTO.getRoomFacilities())
                .bathroomFacilities(roomTypeCreateRequestDTO.getBathroomFacilities())
                .kitchenFacilities(roomTypeCreateRequestDTO.getKitchenFacilities())
                .roomRules(roomTypeCreateRequestDTO.getRoomRules())
                .build();

        // Add the new RoomType to its Property's roomTypes list
        property.getRoomTypes().add(roomType);

        // Save RoomType
        roomType = roomTypeRepository.save(roomType);

        return roomType;
    }

    @Transactional
    public List<RoomType> createRoomTypes(List<RoomTypeCreateRequestDTO> roomTypeCreateRequestDTOs) {
        return roomTypeCreateRequestDTOs.stream()
                .map(this::createRoomType)
                .collect(Collectors.toList());
    }

    public List<RoomType> getRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}