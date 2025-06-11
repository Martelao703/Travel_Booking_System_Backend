package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.PropertyPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.PropertyPatchFieldRules;
import com.david.travel_booking_system.mapper.PropertyMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.*;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.specification.PropertySpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyService {
    // Repositories
    private final PropertyRepository propertyRepository;;
    private final BookingRepository bookingRepository;

    // Parents Repositories
    private final UserRepository userRepository;

    // Children Repositories
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    // Mappers
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, BookingRepository bookingRepository,
                           UserRepository userRepository, RoomTypeRepository roomTypeRepository,
                           RoomRepository roomRepository, BedRepository bedRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
        this.propertyMapper = propertyMapper;
    }

    /* CRUD and Basic Methods -------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(PropertyCreateRequestDTO propertyCreateRequestDTO) {
        Integer ownerId = propertyCreateRequestDTO.getOwnerId();

        // Ensure the owner exists and is not soft-deleted
        Specification<User> ownerSpec = BaseSpecifications.filterById(User.class, ownerId)
                .and(BaseSpecifications.excludeDeleted(User.class));

        // Retrieve the owner
        User owner = userRepository.findOne(ownerSpec)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + ownerId + " not found"));

        // Create Property from DTO
        Property property = propertyMapper.createPropertyFromDTO(propertyCreateRequestDTO);

        // Add the new Property to its Owner's properties list
        owner.getProperties().add(property);

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

    @Transactional(readOnly = true)
    public Property getPropertyByIdWithCollections(Integer id, boolean includeDeleted) {
        Property property = getPropertyById(id, includeDeleted);

        // Load collections
        Hibernate.initialize(property.getAmenities());
        Hibernate.initialize(property.getNearbyServices());
        Hibernate.initialize(property.getHouseRules());
        Hibernate.initialize(property.getRoomTypes());

        return property;
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

    @Transactional
    public void restoreProperty(Integer id) {
        Property property = getPropertyById(id, true);

        // Check if property is soft-deleted
        if (!property.isDeleted()) {
            throw new IllegalStateException("Property is not deleted");
        }

        // Restore property
        property.setDeleted(false);
        propertyRepository.save(property);
    }

    /* Get Lists of Nested Entities */

    @Transactional(readOnly = true)
    public List<Property> getPropertiesByOwnerId(Integer ownerId, boolean includeDeleted) {
        // Ensure the owner exists and is not soft-deleted
        Specification<User> ownerSpec = BaseSpecifications.filterById(User.class, ownerId)
                .and(BaseSpecifications.excludeDeleted(User.class));

        if (!userRepository.exists(ownerSpec)) {
            throw new EntityNotFoundException("User with ID " + ownerId + " not found");
        }

        // Filter by owner ID
        Specification<Property> propertySpec = includeDeleted
                ? PropertySpecifications.filterByOwnerId(ownerId) // Owner ID filter
                : PropertySpecifications.filterByOwnerId(ownerId)
                .and(BaseSpecifications.excludeDeleted(Property.class)); // Owner ID and non-deleted filter

        return propertyRepository.findAll(propertySpec);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    // Check if the Property with the given ID is owned by the user with the given email
    @Transactional(readOnly = true)
    public boolean isOwner(Integer id, String email) {
        // Filter by Property ID and owner email
        Specification<Property> spec = BaseSpecifications.filterById(Property.class, id)
                .and(PropertySpecifications.filterByOwnerEmail(email));

        return propertyRepository.exists(spec);
    }

    @Transactional
    public void activateProperty(Integer id) {
        Property property = getPropertyById(id, true);

        // Check if property is not already active
        if (property.isActive()) {
            throw new IllegalStateException("Property is already active");
        }

        // Activate property
        property.setActive(true);
        propertyRepository.save(property);
    }

    @Transactional
    public void deactivateProperty(Integer id) {
        Property property = getPropertyById(id, true);

        // Check if property is not already deactivated
        if (!property.isActive()) {
            throw new IllegalStateException("Property is already deactivated");
        }

        // Check if property has any rooms with active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot deactivate a property with active bookings");
        }

        // Deactivate property and linked entities
        roomRepository.deactivateByPropertyId(id);
        property.setActive(false);

        propertyRepository.save(property);
    }

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