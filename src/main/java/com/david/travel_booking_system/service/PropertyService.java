package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.PropertyBuilder;
import com.david.travel_booking_system.dto.PropertyDetailDTO;
import com.david.travel_booking_system.dto.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.util.Coordinates;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(@Valid PropertyCreateRequestDTO propertyCreateRequestDTO) {
        // Prepare attributes from DTO
        Coordinates coordinates = new Coordinates(propertyCreateRequestDTO.getLatitude(), propertyCreateRequestDTO.getLongitude());

        // Build Property object from DTO
        Property property = new PropertyBuilder()
                .propertyType(propertyCreateRequestDTO.getPropertyType())
                .name(propertyCreateRequestDTO.getName())
                .city(propertyCreateRequestDTO.getCity())
                .address(propertyCreateRequestDTO.getAddress())
                .isUnderMaintenance(propertyCreateRequestDTO.isUnderMaintenance())
                .coordinates(coordinates)
                .description(propertyCreateRequestDTO.getDescription())
                .stars(propertyCreateRequestDTO.getStars())
                .amenities(propertyCreateRequestDTO.getAmenities())
                .nearbyServices(propertyCreateRequestDTO.getNearbyServices())
                .houseRules(propertyCreateRequestDTO.getHouseRules())
                .build();

        // Save Property
        property = propertyRepository.save(property);

        return property;
    }

    @Transactional(readOnly = true)
    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Property getPropertyById(Integer id) {
        return propertyRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Property with ID " + id + " not found"));
    }

    @Transactional
    public void deleteProperty(Integer id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property with ID " + id + " not found"));
        propertyRepository.delete(property);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}