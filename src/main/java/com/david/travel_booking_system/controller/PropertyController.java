package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.PropertyBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.response.detail.PropertyDetailDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.PropertyFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.PropertyPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.mapper.PropertyMapper;
import com.david.travel_booking_system.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyController(PropertyService propertyService, PropertyMapper propertyMapper) {
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @PostMapping
    public ResponseEntity<PropertyDetailDTO> createProperty(
            @RequestBody @Valid PropertyCreateRequestDTO propertyCreateRequestDTO
    ) {
        PropertyDetailDTO createdProperty = propertyMapper.toDetailDTO(
                propertyService.createProperty(propertyCreateRequestDTO)
        );
        return ResponseEntity.status(201).body(createdProperty); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<PropertyBasicDTO>> getProperties() {
        List<PropertyBasicDTO> properties = propertyMapper.toBasicDTOs(propertyService.getProperties(false));
        return ResponseEntity.ok(properties); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyFullDTO> getProperty(@PathVariable Integer id) {
        PropertyFullDTO property = propertyMapper.toFullDTO(propertyService.getPropertyById(id, false));
        return ResponseEntity.ok(property); // Return 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDetailDTO> updateProperty(
            @PathVariable Integer id,
            @RequestBody @Valid PropertyUpdateRequestDTO propertyUpdateRequestDTO
    ) {
        PropertyDetailDTO updatedPropertyDTO = propertyMapper.toDetailDTO(
                propertyService.updateProperty(id, propertyUpdateRequestDTO)
        );
        return ResponseEntity.ok(updatedPropertyDTO); // Return 200 OK
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PropertyDetailDTO> patchProperty(
            @PathVariable Integer id,
            @RequestBody @Valid PropertyPatchRequestDTO propertyPatchRequestDTO
    ) {
        PropertyDetailDTO patchedPropertyDTO = propertyMapper.toDetailDTO(
                propertyService.patchProperty(id, propertyPatchRequestDTO)
        );
        return ResponseEntity.ok(patchedPropertyDTO); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteProperty(@PathVariable Integer id) {
        propertyService.softDeleteProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProperty(@PathVariable Integer id) {
        propertyService.hardDeleteProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */
}
