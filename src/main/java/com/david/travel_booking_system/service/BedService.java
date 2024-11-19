package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.BedDTO;
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

    @Autowired
    public BedService(BedRepository bedRepository, RoomTypeService roomTypeService) {
        this.bedRepository = bedRepository;
        this.roomTypeService = roomTypeService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Bed createBed(@Valid BedCreateRequestDTO bedCreateRequestDTO) {
        // Find associated RoomType
        RoomType roomType = roomTypeService.getRoomTypeById(bedCreateRequestDTO.getRoomTypeId());

        // Build Bed object from DTO
        Bed bed = new Bed(roomType, bedCreateRequestDTO.getBedType(), bedCreateRequestDTO.getLength(),
                bedCreateRequestDTO.getWidth());

        // Add the new Bed to its RoomType's beds list
        roomType.getBeds().add(bed);

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

    public List<Bed> getBeds() {
        return bedRepository.findAll();
    }

    public Bed getBedById(Integer id) {
        return bedRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Bed with id " + id + " not found"));
    }
}