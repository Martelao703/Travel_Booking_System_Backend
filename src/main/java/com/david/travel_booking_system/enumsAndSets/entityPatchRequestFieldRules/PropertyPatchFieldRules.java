package com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules;

import java.util.Set;

// Fields not present are not patchable
public class PropertyPatchFieldRules {
    // fields that cannot be modified if entity is associated with active bookings
    public static final Set<String> CRITICAL_FIELDS = Set.of(
            "propertyType",
            "city",
            "address",
            "coordinates",
            "stars",
            "amenities",
            "houseRules"
    );

    // fields that cannot be modified if entity is associated with active ongoing bookings
    public static final Set<String> CONDITIONALLY_PATCHABLE_FIELDS = Set.of(
            // no conditionally patchable fields
    );
}
