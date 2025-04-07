package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.BedPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BedUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.BedPatchFieldRules;
import com.david.travel_booking_system.mapper.BedMapper;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.BedRepository;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BedSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BedService {
    // Repositories
    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final RoomTypeRepository roomTypeRepository;

    // Mappers
    private final BedMapper bedMapper;

    @Autowired
    public BedService(BedRepository bedRepository, BookingRepository bookingRepository, PropertyRepository propertyRepository,
                      RoomTypeRepository roomTypeRepository, BedMapper bedMapper) {
        this.bedRepository = bedRepository;
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.bedMapper = bedMapper;
    }

    /* CRUD and Basic Methods -------------------------------------------------------------------------------------- */

    @Transactional
    public Bed createBed(BedCreateRequestDTO bedCreateRequestDTO) {
        Integer id = bedCreateRequestDTO.getRoomTypeId();

        // Ensure the RoomType exists and is not soft-deleted
        Specification<RoomType> roomTypeSpec = BaseSpecifications.filterById(RoomType.class, id)
                .and(BaseSpecifications.excludeDeleted(RoomType.class));

        // Retrieve the RoomType
        RoomType roomType = roomTypeRepository.findOne(roomTypeSpec)
                .orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));

        // Create Bed from DTO
        Bed bed = bedMapper.createBedFromDTO(bedCreateRequestDTO);

        // Add the new Bed to its RoomType's rooms list
        roomType.getBeds().add(bed);

        return bedRepository.save(bed);
    }

    @Transactional(readOnly = true)
    public List<Bed> getBeds(boolean includeDeleted) {
        Specification<Bed> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(Bed.class); // Non-deleted filter

        return bedRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Bed getBedById(Integer id, boolean includeDeleted) {
        Specification<Bed> spec = includeDeleted
                ? BaseSpecifications.filterById(Bed.class, id) // ID filter
                : BaseSpecifications.filterById(Bed.class, id)
                .and(BaseSpecifications.excludeDeleted(Bed.class)); // ID and non-deleted filter

        return bedRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Bed with ID " + id + " not found"));
    }

    @Transactional
    public Bed updateBed(Integer id, BedUpdateRequestDTO bedUpdateRequestDTO) {
        Bed bed = getBedById(id, false);

        // Check if bed is associated with any room types with booked rooms
        if (hasActiveBookings(bed.getRoomType().getId())) {
            throw new IllegalStateException("Cannot update a bed whose room type has booked rooms");
        }

        // Update Bed from DTO
        bedMapper.updateBedFromDTO(bed, bedUpdateRequestDTO);

        return bedRepository.save(bed);
    }

    @Transactional
    public Bed patchBed(Integer id, BedPatchRequestDTO bedPatchRequestDTO) {
        Bed bed = getBedById(id, false);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !BedPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !BedPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = hasActiveBookings(bed.getRoomType().getId());
            if (hasBookings && !BedPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = hasOngoingBookings(bed.getRoomType().getId());
            }
        }

        // Validate and patch Bed
        EntityPatcher.validateAndPatchEntity(
                bed,
                bedPatchRequestDTO,
                BedPatchFieldRules.CRITICAL_FIELDS,
                BedPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS,
                hasBookings,
                hasOngoingBookings
        );

        return bedRepository.save(bed);
    }

    @Transactional
    public void softDeleteBed(Integer id) {
        Bed bed = getBedById(id, false);

        // Check if bed's room type has any rooms with active bookings
        if (hasActiveBookings(bed.getRoomType().getId())) {
            throw new IllegalStateException("Cannot delete a bed whose room type has booked rooms");
        }

        // Soft delete the bed
        bed.setDeleted(true);
        bedRepository.save(bed);
    }

    @Transactional
    public void hardDeleteBed(Integer id) {
        Bed bed = getBedById(id, true);

        // Check if bed is not soft-deleted
        if (!bed.isDeleted()) {
            // Check if bed's room type has any rooms with active bookings
            if (hasActiveBookings(bed.getRoomType().getId())) {
                throw new IllegalStateException("Cannot delete a bed whose room type has booked rooms");
            }
        }

        // Hard delete the bed
        bedRepository.deleteById(id);
    }

    @Transactional
    public void restoreBed(Integer id) {
        Bed bed = getBedById(id, true);

        // Check if bed is soft-deleted
        if (!bed.isDeleted()) {
            throw new IllegalStateException("Bed is not deleted");
        }

        // Restore the bed
        bed.setDeleted(false);
        bedRepository.save(bed);
    }

    /* Get Lists of Nested Entities */

    @Transactional(readOnly = true)
    public List<Bed> getBedsByRoomTypeId(Integer roomTypeId, boolean includeDeleted) {
        // Ensure the room type exists and is not soft-deleted
        Specification<RoomType> roomTypeSpec = BaseSpecifications.filterById(RoomType.class, roomTypeId)
                .and(BaseSpecifications.excludeDeleted(RoomType.class));

        if (!roomTypeRepository.exists(roomTypeSpec)) {
            throw new EntityNotFoundException("RoomType with ID " + roomTypeId + " not found");
        }

        // Filter by room type ID
        Specification<Bed> bedSpec = includeDeleted
                ? BedSpecifications.filterByRoomTypeId(roomTypeId) // RoomType ID filter
                : BedSpecifications.filterByRoomTypeId(roomTypeId)
                .and(BaseSpecifications.excludeDeleted(Bed.class)); // RoomType ID and non-deleted filter

        return bedRepository.findAll(bedSpec);
    }

    @Transactional(readOnly = true)
    public List<Bed> getBedsByPropertyId(Integer propertyId, boolean includeDeleted) {
        // Ensure the property exists and is not soft-deleted
        Specification<Property> propertySpec = BaseSpecifications.filterById(Property.class, propertyId)
                .and(BaseSpecifications.excludeDeleted(Property.class));

        if (!propertyRepository.exists(propertySpec)) {
            throw new EntityNotFoundException("Property with ID " + propertyId + " not found");
        }

        // Filter by property ID
        Specification<Bed> bedSpec = includeDeleted
                ? BedSpecifications.filterByPropertyId(propertyId) // Property ID filter
                : BedSpecifications.filterByPropertyId(propertyId)
                .and(BaseSpecifications.excludeDeleted(Bed.class)); // Property ID and non-deleted filter

        return bedRepository.findAll(bedSpec);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private boolean hasActiveBookings(Integer roomTypeId) {
        // Filter bookings by bed's room type ID and relevant statuses
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomTypeId(roomTypeId)
                .and(BookingSpecifications.filterByStatuses(List.of(
                        BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING
                )));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean hasOngoingBookings(Integer roomTypeId) {
        // Filter bookings by bed's room type ID and ONGOING status
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomTypeId(roomTypeId)
                .and(BookingSpecifications.filterByStatus(BookingStatus.ONGOING));
        return bookingRepository.exists(bookingSpec);
    }
}