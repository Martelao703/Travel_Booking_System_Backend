package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.PropertyBuilder;
import com.david.travel_booking_system.dto.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final BookingService bookingService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, BookingService bookingService) {
        this.propertyRepository = propertyRepository;
        this.bookingService = bookingService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(@Valid PropertyCreateRequestDTO propertyCreateRequestDTO) {
        // Prepare attributes from DTO
        Coordinates coordinates = new Coordinates(propertyCreateRequestDTO.getLatitude(), propertyCreateRequestDTO.getLongitude());

        // Build Property object from DTO
        Property property = new PropertyBuilder()
                .propertyType(propertyCreateRequestDTO.getPropertyType())
                .name(propertyCreateRequestDTO.getName())
                .city(propertyCreateRequestDTO.getCity())
                .address(propertyCreateRequestDTO.getAddress())
                .isUnderMaintenance(propertyCreateRequestDTO.isUnderMaintenance())
                .coordinates(coordinates)
                .description(propertyCreateRequestDTO.getDescription())
                .stars(propertyCreateRequestDTO.getStars())
                .amenities(propertyCreateRequestDTO.getAmenities())
                .nearbyServices(propertyCreateRequestDTO.getNearbyServices())
                .houseRules(propertyCreateRequestDTO.getHouseRules())
                .build();

        // Save Property
        property = propertyRepository.save(property);

        return property;
    }

    @Transactional(readOnly = true)
    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Property getPropertyById(Integer id) {
        return propertyRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Property with ID " + id + " not found"));
    }

    @Transactional
    public void deleteProperty(Integer id) {
        Property property = getPropertyById(id);

        // Check if property has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForProperty(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a property with booked rooms");
        }

        propertyRepository.delete(property);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}