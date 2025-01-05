package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.full.BedFullDTO;
import com.david.travel_booking_system.dto.request.BedRequestDTO;
import com.david.travel_booking_system.mapper.BedMapper;
import com.david.travel_booking_system.service.BedService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/bed")
public class BedController {
    private final BedService bedService;
    private final BedMapper bedMapper;

    @Autowired
    public BedController(BedService bedService, BedMapper bedMapper) {
        this.bedService = bedService;
        this.bedMapper = bedMapper;
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<BedBasicDTO> createBed(@RequestBody @Valid BedRequestDTO bedRequestDTO) {
        BedBasicDTO createdBed = bedMapper.toBasicDTO(bedService.createBed(bedRequestDTO));
        return ResponseEntity.status(201).body(createdBed); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BedBasicDTO>> getBeds() {
        List<BedBasicDTO> beds = bedMapper.toBasicDTOs(bedService.getBeds());
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedFullDTO> getBed(@PathVariable Integer id) {
        BedFullDTO bed = bedMapper.toFullDTO(bedService.getBedById(id));
        return ResponseEntity.ok(bed); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    public ResponseEntity<BedBasicDTO> updateBed(
            @PathVariable Integer id,
            @RequestBody @Valid BedRequestDTO bedRequestDTO
    ) {
        BedBasicDTO updatedBed = bedMapper.toBasicDTO(bedService.updateBed(id, bedRequestDTO));
        return ResponseEntity.ok(updatedBed); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Integer id) {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
