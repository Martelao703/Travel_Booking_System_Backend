package com.david.travel_booking_system.enumsAndSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BedType {
    SINGLE, DOUBLE, QUEEN, KING, TWIN, SOFA, BUNK, DAY, MURPHY, COT, AIR, HAMMOCK, ROUND, EMPEROR, FOURPOSTER, FUTON,
    WATER, TRUNDLE, CANOPY, FULL, TWINXL, DIVAN, POSTER, OTTOMAN, SLEIGH, FRENCH;

    @JsonCreator
    public static BedType fromValue(String value) {
        for (BedType type : BedType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BedType: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
