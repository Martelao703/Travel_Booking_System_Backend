package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.BedDTO;
import com.david.travel_booking_system.dto.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public BedDTO createBed(@RequestBody BedCreateRequestDTO bedCreateRequestDTO) {
        return BedDTO.from(bedService.createBed(bedCreateRequestDTO));
    }

    @GetMapping
    public List<BedDTO> getBeds() {
        return BedDTO.from(bedService.getBeds());
    }

    @GetMapping("/{id}")
    public BedDTO getBed(@PathVariable Integer id) {
        return BedDTO.from(bedService.getBedById(id));
    }
}
