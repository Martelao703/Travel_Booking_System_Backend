package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.request.crud.updateRequest.BedUpdateRequestDTO;
import com.david.travel_booking_system.dto.response.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.response.full.BedFullDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.BedPatchRequestDTO;
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
@RequestMapping("/api/beds")
public class BedController {
    private final BedService bedService;
    private final BedMapper bedMapper;

    @Autowired
    public BedController(BedService bedService, BedMapper bedMapper) {
        this.bedService = bedService;
        this.bedMapper = bedMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<BedBasicDTO> createBed(@RequestBody @Valid BedCreateRequestDTO bedCreateRequestDTO) {
        BedBasicDTO createdBed = bedMapper.toBasicDTO(bedService.createBed(bedCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBed); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BedBasicDTO>> getBeds() {
        List<BedBasicDTO> beds = bedMapper.toBasicDTOs(bedService.getBeds(false));
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BedFullDTO> getBed(@PathVariable Integer id) {
        BedFullDTO bed = bedMapper.toFullDTO(bedService.getBedById(id, false));
        return ResponseEntity.ok(bed); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    public ResponseEntity<BedBasicDTO> updateBed(
            @PathVariable Integer id,
            @RequestBody @Valid BedUpdateRequestDTO bedUpdateRequestDTO
    ) {
        BedBasicDTO updatedBed = bedMapper.toBasicDTO(bedService.updateBed(id, bedUpdateRequestDTO));
        return ResponseEntity.ok(updatedBed); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    public ResponseEntity<BedBasicDTO> patchBed(
            @PathVariable Integer id,
            @RequestBody @Valid BedPatchRequestDTO bedPatchRequestDTO
    ) {
        BedBasicDTO patchedBed = bedMapper.toBasicDTO(bedService.patchBed(id, bedPatchRequestDTO));
        return ResponseEntity.ok(patchedBed); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Integer id) {
        bedService.softDeleteBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteBed(@PathVariable Integer id) {
        bedService.hardDeleteBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreBed(@PathVariable Integer id) {
        bedService.restoreBed(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

}
