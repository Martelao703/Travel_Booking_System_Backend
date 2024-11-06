package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.PropertyDTO;
import com.david.travel_booking_system.dto.PropertyDetailDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.service.PropertyService;
import jakarta.validation.Valid;
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
    public PropertyDetailDTO createProperty(@RequestBody @Valid PropertyDetailDTO propertyDetailDTO) {
        return PropertyDetailDTO.from(propertyService.createProperty(propertyDetailDTO));
    }

    /*
    @GetMapping
    public List<PropertyDTO> getProperties() {
        return propertyService.getProperties();
    }*/
}
