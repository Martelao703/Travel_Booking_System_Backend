package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.request.RoomTypeRequestDTO;
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
    public RoomType createRoomType(RoomTypeRequestDTO roomTypeRequestDTO) {
        // Find associated Property
        Property property = propertyService.getPropertyById(roomTypeRequestDTO.getPropertyId());

        // Build RoomType object from DTO
        RoomType roomType = new RoomTypeBuilder()
                .property(property)
                .name(roomTypeRequestDTO.getName())
                .pricePerNight(roomTypeRequestDTO.getPricePerNight())
                .size(roomTypeRequestDTO.getSize())
                .maxCapacity(roomTypeRequestDTO.getMaxCapacity())
                .hasPrivateBathroom(roomTypeRequestDTO.isHasPrivateBathroom())
                .hasPrivateKitchen(roomTypeRequestDTO.isHasPrivateKitchen())
                .description(roomTypeRequestDTO.getDescription())
                .view(roomTypeRequestDTO.getView())
                .roomFacilities(roomTypeRequestDTO.getRoomFacilities())
                .bathroomFacilities(roomTypeRequestDTO.getBathroomFacilities())
                .kitchenFacilities(roomTypeRequestDTO.getKitchenFacilities())
                .roomRules(roomTypeRequestDTO.getRoomRules())
                .build();

        // Add the new RoomType to its Property's roomTypes list
        property.getRoomTypes().add(roomType);

        // Save RoomType
        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public List<RoomType> createRoomTypes(List<RoomTypeRequestDTO> roomTypeRequestDTOS) {
        return roomTypeRequestDTOS.stream()
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
    public RoomType updateRoomType(Integer id, RoomTypeRequestDTO roomTypeRequestDTO) {
        RoomType roomType = getRoomTypeById(id);

        // Update fields
        roomType.setName(roomTypeRequestDTO.getName());
        roomType.setPricePerNight(roomTypeRequestDTO.getPricePerNight());
        roomType.setSize(roomTypeRequestDTO.getSize());
        roomType.setMaxCapacity(roomTypeRequestDTO.getMaxCapacity());
        roomType.setHasPrivateBathroom(roomTypeRequestDTO.isHasPrivateBathroom());
        roomType.setHasPrivateKitchen(roomTypeRequestDTO.isHasPrivateKitchen());
        roomType.setDescription(roomTypeRequestDTO.getDescription());
        roomType.setView(roomTypeRequestDTO.getView());

        roomType.setRoomFacilities(new ArrayList<>(roomTypeRequestDTO.getRoomFacilities()));
        roomType.setBathroomFacilities(new ArrayList<>(roomTypeRequestDTO.getBathroomFacilities()));
        roomType.setKitchenFacilities(new ArrayList<>(roomTypeRequestDTO.getKitchenFacilities()));
        roomType.setRoomRules(new ArrayList<>(roomTypeRequestDTO.getRoomRules()));

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