package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.BedDTO;
import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.BedRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedService {
    private final BedRepository bedRepository;

    @Autowired
    public BedService(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Bed createBed(@Valid BedDTO bedDTO, RoomType roomType) {
        // Build Bed object from DTO
        Bed bed = new Bed(roomType, bedDTO.getBedType(), bedDTO.getLength(), bedDTO.getWidth());

        return bedRepository.save(bed);
    }

    @Transactional
    public List<Bed> createBeds(List<BedDTO> bedDTOs, RoomType roomType) {
        return bedDTOs.stream()
                .map(bedDTO -> createBed(bedDTO, roomType))
                .collect(Collectors.toList());
    }
}