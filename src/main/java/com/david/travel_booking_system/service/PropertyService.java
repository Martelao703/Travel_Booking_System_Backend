package com.david.travel_booking_system.service;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}
