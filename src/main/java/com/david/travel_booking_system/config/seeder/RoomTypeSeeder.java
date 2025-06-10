package com.david.travel_booking_system.config.seeder;

import com.david.travel_booking_system.config.DataSeeder;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@Order(3)
public class RoomTypeSeeder implements DataSeeder {

    private final RoomTypeService roomTypeService;

    public RoomTypeSeeder(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @Override
    public void seed() {
        /* Room Types for Admin Property */

        // Id - 1
        seedRoomType(1, "Single Room", 50.0, 30.0, 1, false,
                false, "Comfortable room with essential amenities.", "Garden View",
                List.of("Air Conditioning", "Free Wi-Fi"),
                List.of("Shower"),
                List.of("Refrigerator"),
                List.of("Quiet hours after 11 PM"));

        // Id - 2
        seedRoomType(1, "Deluxe Suite", 200.0, 60.0, 2, true,
                true, "Luxurious suite with city views and modern amenities.", "City View",
                List.of("Air Conditioning", "Free Wi-Fi", "Flat-screen TV"),
                List.of("Shower", "Bathtub", "Hairdryer"),
                List.of("Microwave", "Refrigerator", "Coffee Maker"),
                List.of("No smoking", "No pets"));

        /* Room Types for Host Property */

        // Id - 3
        seedRoomType(2, "2-Bedroom Apartment", 80.0, 40.0, 4,
                true, true,
                "Two bedroom apartment, with two single beds in one room, and a double bed on the other.",
                "River View",
                List.of("Free Wi-Fi", "Balcony"),
                List.of("Shower"),
                List.of("Kitchenette"),
                List.of("No parties allowed"));
    }

    private void seedRoomType(Integer propertyId,
                              String name,
                              double pricePerNight,
                              double size,
                              int maxCapacity,
                              boolean hasPrivateBathroom,
                              boolean hasPrivateKitchen,
                              String description,
                              String view,
                              List<String> roomFacilities,
                              List<String> bathroomFacilities,
                              List<String> kitchenFacilities,
                              List<String> roomRules) {

        // Build RoomTypeCreateRequestDTO
        RoomTypeCreateRequestDTO dto = RoomTypeCreateRequestDTO.builder()
                .propertyId(propertyId)
                .name(name)
                .pricePerNight(pricePerNight)
                .size(size)
                .maxCapacity(maxCapacity)
                .hasPrivateBathroom(hasPrivateBathroom)
                .hasPrivateKitchen(hasPrivateKitchen)
                .description(description)
                .view(view)
                .roomFacilities(new ArrayList<>(roomFacilities))
                .bathroomFacilities(new ArrayList<>(bathroomFacilities))
                .kitchenFacilities(new ArrayList<>(kitchenFacilities))
                .roomRules(new ArrayList<>(roomRules))
                .build();

        // Create the room type
        roomTypeService.createRoomType(dto);
    }
}
