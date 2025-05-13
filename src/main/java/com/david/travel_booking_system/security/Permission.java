package com.david.travel_booking_system.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    // Property
    PROPERTY_READ("property:read"),
    PROPERTY_CREATE("property:create"),
    PROPERTY_UPDATE("property:update"),
    PROPERTY_DELETE("property:delete"),
    PROPERTY_RESTORE("property:restore"),
    PROPERTY_ACTIVATE("property:activate"),
    PROPERTY_DEACTIVATE("property:deactivate"),
    PROPERTY_READ_BOOKINGS("property:read-bookings"),

    // Room Type
    ROOM_TYPE_READ("roomType:read"),
    ROOM_TYPE_CREATE("roomType:create"),
    ROOM_TYPE_UPDATE("roomType:update"),
    ROOM_TYPE_DELETE("roomType:delete"),
    ROOM_TYPE_RESTORE("roomType:restore"),

    // Room
    ROOM_READ("room:read"),
    ROOM_CREATE("room:create"),
    ROOM_UPDATE("room:update"),
    ROOM_DELETE("room:delete"),
    ROOM_RESTORE("room:restore"),
    ROOM_ACTIVATE("room:activate"),
    ROOM_DEACTIVATE("room:deactivate"),
    ROOM_READ_BOOKINGS("room:read-bookings"),

    // Bed
    BED_READ("bed:read"),
    BED_CREATE("bed:create"),
    BED_UPDATE("bed:update"),
    BED_DELETE("bed:delete"),
    BED_RESTORE("bed:restore"),

    // Booking
    BOOKING_READ("booking:read"),
    BOOKING_CREATE("booking:create"),
    BOOKING_UPDATE("booking:update"),
    BOOKING_DELETE("booking:delete"),
    BOOKING_RESTORE("booking:restore"),
    BOOKING_CHANGE_DATES("booking:change-dates"),
    BOOKING_CONFIRM_PAYMENT("booking:confirm-payment"),
    BOOKING_CHECK_IN("booking:check-in"),
    BOOKING_CHECK_OUT("booking:check-out"),
    BOOKING_CANCEL("booking:cancel"),
    BOOKING_REJECT("booking:reject"),

    // User
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    USER_RESTORE("user:restore"),
    USER_ACTIVATE("user:activate"),
    USER_DEACTIVATE("user:deactivate");

    @Getter
    private final String permission;
}
