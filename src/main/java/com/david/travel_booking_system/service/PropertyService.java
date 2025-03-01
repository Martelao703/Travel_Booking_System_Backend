package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.PropertyPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.PropertyPatchFieldRules;
import com.david.travel_booking_system.mapper.PropertyMapper;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final BookingValidationService bookingValidationService;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, BookingValidationService bookingValidationService,
                           PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.bookingValidationService = bookingValidationService;
        this.propertyMapper = propertyMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(PropertyCreateRequestDTO propertyCreateRequestDTO) {
        // Create Property from DTO
        Property property = propertyMapper.createPropertyFromDTO(propertyCreateRequestDTO);

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
        boolean hasBookings = bookingValidationService.existsBookingsForProperty(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot update a property with booked rooms");
        }

        // Update property from DTO
        propertyMapper.updatePropertyFromDTO(property, propertyUpdateRequestDTO);

        return propertyRepository.save(property);
    }

    @Transactional
    public Property patchProperty(Integer id, PropertyPatchRequestDTO propertyPatchRequestDTO) {
        Property property = getPropertyById(id);

        // Booking conditions
        boolean hasAnyBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !PropertyPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !PropertyPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasAnyBookings = bookingValidationService.existsBookingsForProperty(id);
            if (hasAnyBookings && !PropertyPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = bookingValidationService.existsOngoingBookingsForProperty(id);
            }
        }

        // Validate and patch property
        EntityPatcher.validateAndPatchEntity(
                property,
                propertyPatchRequestDTO,
                PropertyPatchFieldRules.CRITICAL_FIELDS,
                PropertyPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS,
                hasAnyBookings,
                hasOngoingBookings
        );

        return propertyRepository.save(property);
    }

    @Transactional
    public void deleteProperty(Integer id) {
        Property property = getPropertyById(id);

        // Check if property has any booked rooms
        boolean hasBookings = bookingValidationService.existsBookingsForProperty(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a property with booked rooms");
        }

        propertyRepository.delete(property);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}