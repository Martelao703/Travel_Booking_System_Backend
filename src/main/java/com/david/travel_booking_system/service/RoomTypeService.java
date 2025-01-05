package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.request.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final BookingService bookingService;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, PropertyService propertyService, BookingService bookingService) {
        this.roomTypeRepository = roomTypeRepository;
        this.propertyService = propertyService;
        this.bookingService = bookingService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public RoomType createRoomType(RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        // Find associated Property
        Property property = propertyService.getPropertyById(roomTypeCreateRequestDTO.getPropertyId());

        // Build RoomType object from DTO
        RoomType roomType = new RoomTypeBuilder()
                .property(property)
                .name(roomTypeCreateRequestDTO.getName())
                .pricePerNight(roomTypeCreateRequestDTO.getPricePerNight())
                .size(roomTypeCreateRequestDTO.getSize())
                .maxCapacity(roomTypeCreateRequestDTO.getMaxCapacity())
                .hasPrivateBathroom(roomTypeCreateRequestDTO.hasPrivateBathroom())
                .hasPrivateKitchen(roomTypeCreateRequestDTO.hasPrivateKitchen())
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
        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public List<RoomType> createRoomTypes(List<RoomTypeCreateRequestDTO> roomTypeCreateRequestDTOs) {
        return roomTypeCreateRequestDTOs.stream()
                .map(this::createRoomType)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));
    }

    @Transactional
    public RoomType updateRoomType(Integer id, RoomTypeUpdateRequestDTO roomTypeUpdateRequestDTO) {
        RoomType roomType = getRoomTypeById(id);

        // Update fields
        roomType.setName(roomTypeUpdateRequestDTO.getName());
        roomType.setPricePerNight(roomTypeUpdateRequestDTO.getPricePerNight());
        roomType.setSize(roomTypeUpdateRequestDTO.getSize());
        roomType.setMaxCapacity(roomTypeUpdateRequestDTO.getMaxCapacity());
        roomType.setHasPrivateBathroom(roomTypeUpdateRequestDTO.hasPrivateBathroom());
        roomType.setHasPrivateKitchen(roomTypeUpdateRequestDTO.hasPrivateKitchen());
        roomType.setDescription(roomTypeUpdateRequestDTO.getDescription());
        roomType.setView(roomTypeUpdateRequestDTO.getView());

        roomType.setRoomFacilities(new ArrayList<>(roomTypeUpdateRequestDTO.getRoomFacilities()));
        roomType.setBathroomFacilities(new ArrayList<>(roomTypeUpdateRequestDTO.getBathroomFacilities()));
        roomType.setKitchenFacilities(new ArrayList<>(roomTypeUpdateRequestDTO.getKitchenFacilities()));
        roomType.setRoomRules(new ArrayList<>(roomTypeUpdateRequestDTO.getRoomRules()));

        // Save updated RoomType
        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public void deleteRoomType(Integer id) {
        RoomType roomType = getRoomTypeById(id);

        // Check if room type has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForRoomType(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a room type with booked rooms");
        }

        // Delete RoomType
        roomTypeRepository.delete(roomType);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}