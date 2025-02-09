package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.BedRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.BedPatchRequestDTO;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.BedPatchFieldRules;
import com.david.travel_booking_system.mapper.BedMapper;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.repository.BedRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BedService {
    private final BedRepository bedRepository;
    private final BookingService bookingService;
    private final BedMapper bedMapper;

    @Autowired
    public BedService(BedRepository bedRepository, BookingService bookingService, BedMapper bedMapper) {
        this.bedRepository = bedRepository;
        this.bookingService = bookingService;
        this.bedMapper = bedMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Bed createBed(BedRequestDTO bedRequestDTO) {
        // Create Bed from DTO
        Bed bed = bedMapper.createBedFromDTO(bedRequestDTO);

        return bedRepository.save(bed);
    }

    @Transactional(readOnly = true)
    public List<Bed> getBeds() {
        return bedRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Bed getBedById(Integer id) {
        return bedRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Bed with id " + id + " not found"));
    }

    @Transactional
    public Bed updateBed(Integer id, BedRequestDTO bedRequestDTO) {
        Bed bed = getBedById(id);

        // Check if bed is associated with any room types with booked rooms
        boolean hasBookings = bookingService.existsBookingsForBed(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot update a bed associated with booked rooms");
        }

        // Update Bed from DTO
        bedMapper.updateBedFromDTO(bed, bedRequestDTO);

        return bedRepository.save(bed);
    }

    @Transactional
    public Bed patchBed(Integer id, BedPatchRequestDTO bedPatchRequestDTO) {
        Bed bed = getBedById(id);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !BedPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !BedPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = bookingService.existsBookingsForBed(id);
            if (hasBookings && !BedPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = bookingService.existsOngoingBookingsForBed(id);
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
    public void deleteBed(Integer id) {
        Bed bed = getBedById(id);

        // Check if bed is associated with any room types with booked rooms
        boolean hasBookings = bookingService.existsBookingsForBed(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a bed associated with booked rooms");
        }

        bedRepository.delete(bed);
    }
}