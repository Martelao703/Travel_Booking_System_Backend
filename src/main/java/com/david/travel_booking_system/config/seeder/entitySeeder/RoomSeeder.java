package com.david.travel_booking_system.config.seeder.entitySeeder;

import com.david.travel_booking_system.config.seeder.DataSeeder;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.service.RoomService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(4)
public class RoomSeeder implements DataSeeder {

    private final RoomService roomService;

    public RoomSeeder(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void seed() {
        /* ------------------------ Rooms for Admin Property ------------------------ */

        /* Rooms for Room Type 1 - Single Room */

        // Id - 1
        seedRoom(1, 1, true, false);
        // Id - 2
        seedRoom(1, 2, true, false);
        // Id - 3
        seedRoom(1, 3, false, true);

        /* Rooms for Room Type 2 - Deluxe Suit */

        // Id - 4
        seedRoom(2, 1, true, false);
        // Id - 5
        seedRoom(2, 2, true, false);
        // Id - 6
        seedRoom(2, 3, false, true);

        /* ------------------------ Rooms for Host Property ------------------------ */

        /* Rooms for Room Type 3 - 2-Bedroom Apartment */

        // Id - 7
        seedRoom(3, 1, true, false);
        // Id - 8
        seedRoom(3, 2, true, false);

    }

    private void seedRoom(Integer roomTypeId,
                          Integer floorNumber,
                          boolean cleaned,
                          boolean underMaintenance) {

        // Build RoomCreateRequestDTO
        RoomCreateRequestDTO roomCreateRequestDTO = RoomCreateRequestDTO.builder()
                .roomTypeId(roomTypeId)
                .floorNumber(floorNumber)
                .cleaned(cleaned)
                .underMaintenance(underMaintenance)
                .build();

        // Save the room
        roomService.createRoom(roomCreateRequestDTO);
    }
}
