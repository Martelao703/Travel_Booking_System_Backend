package com.david.travel_booking_system.enumsAndSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyType {
    Hotel, Apartment, Resort, Villa, Cabin, Cottage, GlampingSite, ServicedApartment, VacationHome, GuestHouse, Hostel,
    Motel, BandB, Ryokan, ResortVillage, HomeStay, Campground, CountryHouse, FarmStay, Boat, LuxuryTent, SelfCateringAccommodation,
    TinyHouse, Aparthotel, Chalet, Inn, Lodge;

    @JsonCreator
    public static PropertyType fromValue(String value) {
        for (PropertyType type : PropertyType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PropertyType: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
