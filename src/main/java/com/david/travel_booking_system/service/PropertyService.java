package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.PropertyBuilder;
import com.david.travel_booking_system.builder.RoomTypeBuilder;
import com.david.travel_booking_system.dto.PropertyDetailDTO;
import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.util.Coordinates;
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
    private final RoomTypeService roomTypeService;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, RoomTypeService roomTypeService) {
        this.propertyRepository = propertyRepository;
        this.roomTypeService = roomTypeService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Property createProperty(PropertyDetailDTO propertyDetailDTO) {
        // Prepare flattened attributes from DTO
        Coordinates coordinates = new Coordinates(propertyDetailDTO.getLatitude(), propertyDetailDTO.getLongitude());

        // Build Property object from DTO
        Property property = new PropertyBuilder()
                .name(propertyDetailDTO.getName())
                .city(propertyDetailDTO.getCity())
                .address(propertyDetailDTO.getAddress())
                .coordinates(coordinates)
                .description(propertyDetailDTO.getDescription())
                .stars(propertyDetailDTO.getStars())
                .userRating(propertyDetailDTO.getUserRating())
                .amenities(propertyDetailDTO.getAmenities())
                .nearbyServices(propertyDetailDTO.getNearbyServices())
                .houseRules(propertyDetailDTO.getHouseRules())
                .build();

        // Save Property to generate ID and associate it with RoomTypes
        property = propertyRepository.save(property);

        // Delegate RoomType creation to RoomTypeService
        property.setRoomTypes(roomTypeService.createRoomTypes(propertyDetailDTO.getRoomTypes(), property));

        return property;
    }

    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}