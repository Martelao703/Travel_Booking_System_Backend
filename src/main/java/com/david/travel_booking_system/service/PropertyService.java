package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.PropertyPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.PropertyPatchFieldRules;
import com.david.travel_booking_system.mapper.PropertyMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.*;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.util.BookingServiceHelper;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {
    // Repositories
    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    // Mappers
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, BookingRepository bookingRepository,
                           RoomTypeRepository roomTypeRepository, RoomRepository roomRepository, BedRepository bedRepository,
                           PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
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
    public List<Property> getProperties(boolean includeDeleted) {
        Specification<Property> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(Property.class); // Non-deleted filter

        return propertyRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Property getPropertyById(Integer id, boolean includeDeleted) {
        Specification<Property> spec = includeDeleted
                ? BaseSpecifications.filterById(Property.class, id) // ID filter
                : BaseSpecifications.filterById(Property.class, id)
                .and(BaseSpecifications.excludeDeleted(Property.class)); // ID and non-deleted filter

        return propertyRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Property with ID " + id + " not found"));
    }

    @Transactional
    public Property updateProperty(Integer id, PropertyUpdateRequestDTO propertyUpdateRequestDTO) {
        Property property = getPropertyById(id, false);

        // Check if property has any booked rooms
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot update a property with booked rooms");
        }

        // Update property from DTO
        propertyMapper.updatePropertyFromDTO(property, propertyUpdateRequestDTO);

        return propertyRepository.save(property);
    }

    @Transactional
    public Property patchProperty(Integer id, PropertyPatchRequestDTO propertyPatchRequestDTO) {
        Property property = getPropertyById(id, false);

        // Booking conditions
        boolean hasAnyBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !PropertyPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !PropertyPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasAnyBookings = hasActiveBookings(id);
            if (hasAnyBookings && !PropertyPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = hasOngoingBookings(id);
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
    public void softDeleteProperty(Integer id) {
        Property property = getPropertyById(id, false);

        // Check if property has any rooms with active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot delete a property with active bookings");
        }

        // Soft delete all linked entities
        bedRepository.softDeleteByPropertyId(id);
        roomRepository.softDeleteByPropertyId(id);
        roomTypeRepository.softDeleteByPropertyId(id);

        // Soft delete property
        property.setActive(false);
        property.setDeleted(true);
        propertyRepository.save(property);
    }

    @Transactional
    public void hardDeleteProperty(Integer id) {
        Property property = getPropertyById(id, true);

        // Check if property is not soft-deleted
        if (!property.isDeleted()) {
            // Check if property has any rooms with active bookings
            if (hasActiveBookings(id)) {
                throw new IllegalStateException("Cannot delete a property with active bookings");
            }
        }

        // Hard delete property (CascadeType.ALL will handle RoomType, Room and Bed deletion)
        propertyRepository.deleteById(id);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */



    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private boolean hasActiveBookings(Integer id) {
        // Filter bookings by property ID and relevant statuses
        Specification<Booking> bookingSpec = BookingSpecifications.filterByPropertyId(id)
                .and(BookingSpecifications.filterByStatuses(List.of(
                        BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING
                )));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean hasOngoingBookings(Integer id) {
        // Filter bookings by property ID and ONGOING status
        Specification<Booking> bookingSpec = BookingSpecifications.filterByPropertyId(id)
                .and(BookingSpecifications.filterByStatus(BookingStatus.ONGOING));

        return bookingRepository.exists(bookingSpec);
    }
}