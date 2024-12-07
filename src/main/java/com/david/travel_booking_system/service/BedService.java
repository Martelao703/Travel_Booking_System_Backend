package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.BedRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedService {
    private final BedRepository bedRepository;
    private final RoomTypeService roomTypeService;
    private final BookingService bookingService;

    @Autowired
    public BedService(BedRepository bedRepository, RoomTypeService roomTypeService, BookingService bookingService) {
        this.bedRepository = bedRepository;
        this.roomTypeService = roomTypeService;
        this.bookingService = bookingService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Bed createBed(@Valid BedCreateRequestDTO bedCreateRequestDTO) {
        // Build Bed object from DTO
        Bed bed = new Bed(bedCreateRequestDTO.getBedType(), bedCreateRequestDTO.getLength(),
                bedCreateRequestDTO.getWidth());

        // Save Bed
        bed = bedRepository.save(bed);

        return bed;
    }

    @Transactional
    public List<Bed> createBeds(List<BedCreateRequestDTO> bedCreateRequestDTOs) {
        return bedCreateRequestDTOs.stream()
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