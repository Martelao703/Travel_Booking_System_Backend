package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.PropertyDTO;
import com.david.travel_booking_system.dto.PropertyDetailDTO;
import com.david.travel_booking_system.dto.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public PropertyDetailDTO createProperty(@RequestBody PropertyCreateRequestDTO propertyCreateRequestDTO) {
        return PropertyDetailDTO.from(propertyService.createProperty(propertyCreateRequestDTO));
    }

    @GetMapping
    public List<PropertyDTO> getProperties() {
        return PropertyDTO.from(propertyService.getProperties());
    }

    @GetMapping("/{id}")
    public PropertyDetailDTO getProperty(@PathVariable Integer id) {
        return PropertyDetailDTO.from(propertyService.getPropertyById(id));
    }
}
