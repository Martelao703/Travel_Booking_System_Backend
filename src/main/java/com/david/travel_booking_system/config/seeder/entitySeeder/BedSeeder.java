package com.david.travel_booking_system.config.seeder.entitySeeder;

import com.david.travel_booking_system.config.seeder.DataSeeder;
import com.david.travel_booking_system.dto.request.crud.createRequest.BedCreateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BedType;
import com.david.travel_booking_system.service.BedService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(5)
public class BedSeeder implements DataSeeder {

    private final BedService bedService;

    public BedSeeder(BedService bedService) {
        this.bedService = bedService;
    }

    @Override
    public void seed() {
        /* ------------------------ Beds for Admin Property ------------------------ */

        /* Beds for Room Type 1 - Single Room */

        // Id - 1
        seedBed(1, BedType.SINGLE, 190.0, 90.0);

        /* Beds for Room Type 2 - Deluxe Suite */

        // Id - 2
        seedBed(2, BedType.KING, 200.0, 200.0);

        /* ------------------------ Beds for Host Property ------------------------ */

        /* Beds for Room Type 3 - 2-Bedroom Apartment */

        // Id - 3
        seedBed(3, BedType.SINGLE, 190.0, 90.0);
        // Id - 4
        seedBed(3, BedType.SINGLE, 190.0, 90.0);
        // Id - 5
        seedBed(3, BedType.DOUBLE, 200.0, 140.0);
    }

    private void seedBed(Integer roomTypeId,
                         BedType bedType,
                         Double length,
                         Double width) {

        // Build BedCreateRequestDTO
        BedCreateRequestDTO bedCreateRequestDTO = BedCreateRequestDTO.builder()
                .roomTypeId(roomTypeId)
                .bedType(bedType)
                .length(length)
                .width(width)
                .build();

        // Save the bed
        bedService.createBed(bedCreateRequestDTO);
    }
}
