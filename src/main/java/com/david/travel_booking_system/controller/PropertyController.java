package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.PropertyBasicDTO;
import com.david.travel_booking_system.dto.detail.PropertyDetailDTO;
import com.david.travel_booking_system.dto.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.full.PropertyFullDTO;
import com.david.travel_booking_system.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<PropertyDetailDTO> createProperty(@RequestBody PropertyCreateRequestDTO propertyCreateRequestDTO) {
        PropertyDetailDTO createdProperty = PropertyDetailDTO.from(propertyService.createProperty(propertyCreateRequestDTO));
        return ResponseEntity.status(201).body(createdProperty); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<PropertyBasicDTO>> getProperties() {
        List<PropertyBasicDTO> properties = PropertyBasicDTO.from(propertyService.getProperties());
        return ResponseEntity.ok(properties); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyFullDTO> getProperty(@PathVariable Integer id) {
        PropertyFullDTO property = PropertyFullDTO.from(propertyService.getPropertyById(id));
        return ResponseEntity.ok(property); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
