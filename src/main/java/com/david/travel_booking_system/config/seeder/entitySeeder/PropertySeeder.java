package com.david.travel_booking_system.config.seeder.entitySeeder;

import com.david.travel_booking_system.config.seeder.DataSeeder;
import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.PropertyType;
import com.david.travel_booking_system.service.PropertyService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@Order(2)
public class PropertySeeder implements DataSeeder {

    private final PropertyService propertyService;

    public PropertySeeder(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public void seed() {
        // Admin Property
        // Id - 1
        seedProperty(1, PropertyType.Hotel, "Admin Hotel", "Lisbon", "Avenida da Liberdade, 10",
                false,  38.7223, -9.1393,
                "A luxurious 5-star hotel in the heart of Lisbon, with panoramic city views.", 5,
                List.of("Free Wi-Fi", "Rooftop pool", "Gym", "Spa"),
                List.of("Metro station (200m)", "Pharmacy (100m)", "Airport shuttle"),
                List.of("No smoking", "No pets", "Quiet hours after 11 PM")
        );

        // Host Property
        // Id - 2
        seedProperty(3, PropertyType.Apartment, "Host Apartment", "Porto", "Rua das Flores, 23",
                false, 41.1496, -8.6109,
                "Charming riverside apartment with modern amenities and balcony overlooking the Douro.", 4,
                List.of("Kitchenette", "Washer & dryer", "High-speed internet"),
                List.of("Bus stop (50m)", "Supermarket (150m)", "Coffee shop"),
                List.of("No parties", "Quiet hours after 11 PM")
        );
    }

    private void seedProperty(Integer ownerId,
                              PropertyType type,
                              String name,
                              String city,
                              String address,
                              boolean underMaintenance,
                              double latitude,
                              double longitude,
                              String description,
                              int stars,
                              List<String> amenities,
                              List<String> nearbyServices,
                              List<String> houseRules) {

        // Build PropertyCreateRequestDTO
        PropertyCreateRequestDTO dto = PropertyCreateRequestDTO.builder()
                .ownerId(ownerId)
                .propertyType(type)
                .name(name)
                .city(city)
                .address(address)
                .underMaintenance(underMaintenance)
                .latitude(latitude)
                .longitude(longitude)
                .description(description)
                .stars(stars)
                .amenities(new ArrayList<>(amenities))
                .nearbyServices(new ArrayList<>(nearbyServices))
                .houseRules(new ArrayList<>(houseRules))
                .build();

        // Create property
        propertyService.createProperty(dto);
    }
}
