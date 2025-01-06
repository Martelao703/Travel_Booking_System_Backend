package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.BedRequestDTO;
import com.david.travel_booking_system.mapper.BedMapper;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.repository.BedRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        // Save Bed
        return bedRepository.save(bed);
    }

    @Transactional
    public List<Bed> createBeds(List<BedRequestDTO> bedRequestDTOS) {
        return bedRequestDTOS.stream()
                .map(this::createBed)
                .collect(Collectors.toList());
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

        // Update Bed from DTO
        bedMapper.updateBedFromDTO(bed, bedRequestDTO);

        // Save Bed
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

        // Delete Bed
        bedRepository.delete(bed);
    }
}