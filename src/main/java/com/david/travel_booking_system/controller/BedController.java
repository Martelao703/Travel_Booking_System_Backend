package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.BedDTO;
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
    public ResponseEntity<BedDTO> createBed(@RequestBody BedCreateRequestDTO bedCreateRequestDTO) {
        BedDTO createdBed = BedDTO.from(bedService.createBed(bedCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBed); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BedDTO>> getBeds() {
        List<BedDTO> beds = BedDTO.from(bedService.getBeds());
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedDTO> getBed(@PathVariable Integer id) {
        BedDTO bed = BedDTO.from(bedService.getBedById(id));
        return ResponseEntity.ok(bed); // Return 200 OK
    }
}
