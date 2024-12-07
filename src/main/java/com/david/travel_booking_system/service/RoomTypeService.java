package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteRoomType(Integer id) {
        RoomType roomType = getRoomTypeById(id);

        // Check if room type has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForRoomType(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a room type with booked rooms");
        }

        roomTypeRepository.delete(roomType);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}