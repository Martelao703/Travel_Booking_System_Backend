package com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules;

import java.util.Set;

public class RoomTypePatchFieldRules {
    // fields that cannot be modified if entity is associated with active bookings
    public static final Set<String> CRITICAL_FIELDS = Set.of(
            "pricePerNight",
            "size",
            "maxCapacity",
            "hasPrivateBathroom",
            "hasPrivateKitchen",
            "roomFacilities",
            "bathroomFacilities",
            "kitchenFacilities",
            "roomRules"
    );

    // fields that cannot be modified if entity is associated with active ongoing bookings
    public static final Set<String> CONDITIONALLY_PATCHABLE_FIELDS = Set.of(
            // no conditionally patchable fields
    );
}
