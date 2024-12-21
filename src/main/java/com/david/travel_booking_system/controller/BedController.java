package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.full.BedFullDTO;
import com.david.travel_booking_system.dto.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.service.BedService;
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

    @Autowired
    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<BedBasicDTO> createBed(@RequestBody BedCreateRequestDTO bedCreateRequestDTO) {
        BedBasicDTO createdBed = BedBasicDTO.from(bedService.createBed(bedCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBed); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BedBasicDTO>> getBeds() {
        List<BedBasicDTO> beds = BedBasicDTO.from(bedService.getBeds());
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedFullDTO> getBed(@PathVariable Integer id) {
        BedFullDTO bed = BedFullDTO.from(bedService.getBedById(id));
        return ResponseEntity.ok(bed); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Integer id) {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
