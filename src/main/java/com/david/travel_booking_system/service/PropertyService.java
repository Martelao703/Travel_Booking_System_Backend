package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.PropertyBuilder;
import com.david.travel_booking_system.dto.request.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.mapper.PropertyMapper;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final BookingService bookingService;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, BookingService bookingService, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.bookingService = bookingService;
        this.propertyMapper = propertyMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(PropertyCreateRequestDTO propertyCreateRequestDTO) {
        // Create Property from DTO
        Property property = propertyMapper.createPropertyFromDTO(propertyCreateRequestDTO);

        // Save Property
        return propertyRepository.save(property);
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
    public Property updateProperty(Integer id, PropertyUpdateRequestDTO propertyUpdateRequestDTO) {
        Property property = getPropertyById(id);

        // Check if property has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForProperty(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot update a property with booked rooms");
        }

        // Update property from DTO
        propertyMapper.updatePropertyFromDTO(property, propertyUpdateRequestDTO);

        // Save updated property
        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteProperty(Integer id) {
        Property property = getPropertyById(id);

        // Check if property has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForProperty(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a property with booked rooms");
        }

        // Delete property
        propertyRepository.delete(property);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}