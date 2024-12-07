package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.BedDTO;
import com.david.travel_booking_system.dto.detail.BedDetailDTO;
import com.david.travel_booking_system.dto.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bed")
public class BedController {
    private final BedService bedService;

    @Autowired
    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    @PostMapping
    public ResponseEntity<BedDetailDTO> createBed(@RequestBody BedCreateRequestDTO bedCreateRequestDTO) {
        BedDetailDTO createdBed = BedDetailDTO.from(bedService.createBed(bedCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBed); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BedDTO>> getBeds() {
        List<BedDTO> beds = BedDTO.from(bedService.getBeds());
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedDetailDTO> getBed(@PathVariable Integer id) {
        BedDetailDTO bed = BedDetailDTO.from(bedService.getBedById(id));
        return ResponseEntity.ok(bed); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Integer id) {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
