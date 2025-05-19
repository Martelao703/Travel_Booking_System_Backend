package com.david.travel_booking_system.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    /* --- Property --- */

    //OWN
    PROPERTY_READ_OWN("property:read:own"),
    PROPERTY_CREATE_OWN("property:create:own"),
    PROPERTY_UPDATE_OWN("property:update:own"),
    PROPERTY_DELETE_OWN("property:delete:own"),
    PROPERTY_RESTORE_OWN("property:restore:own"),
    PROPERTY_ACTIVATE_OWN("property:activate:own"),
    PROPERTY_DEACTIVATE_OWN("property:deactivate:own"),
    PROPERTY_READ_BOOKINGS_OWN("property:read-bookings:own"),

    //ANY
    PROPERTY_READ_ANY("property:read:any"),
    PROPERTY_CREATE_ANY("property:create:any"),
    PROPERTY_UPDATE_ANY("property:update:any"),
    PROPERTY_DELETE_ANY("property:delete:any"),
    PROPERTY_RESTORE_ANY("property:restore:any"),
    PROPERTY_ACTIVATE_ANY("property:activate:any"),
    PROPERTY_DEACTIVATE_ANY("property:deactivate:any"),
    PROPERTY_READ_BOOKINGS_ANY("property:read-bookings:any"),

    /* --- Room Type --- */

    //OWN
    ROOM_TYPE_READ_OWN("roomType:read:own"),
    ROOM_TYPE_CREATE_OWN("roomType:create:own"),
    ROOM_TYPE_UPDATE_OWN("roomType:update:own"),
    ROOM_TYPE_DELETE_OWN("roomType:delete:own"),
    ROOM_TYPE_RESTORE_OWN("roomType:restore:own"),

    //ANY
    ROOM_TYPE_READ_ANY("roomType:read:any"),
    ROOM_TYPE_CREATE_ANY("roomType:create:any"),
    ROOM_TYPE_UPDATE_ANY("roomType:update:any"),
    ROOM_TYPE_DELETE_ANY("roomType:delete:any"),
    ROOM_TYPE_RESTORE_ANY("roomType:restore:any"),

    /* --- Room --- */

    //OWN
    ROOM_READ_OWN("room:read:own"),
    ROOM_CREATE_OWN("room:create:own"),
    ROOM_UPDATE_OWN("room:update:own"),
    ROOM_DELETE_OWN("room:delete:own"),
    ROOM_RESTORE_OWN("room:restore:own"),
    ROOM_ACTIVATE_OWN("room:activate:own"),
    ROOM_DEACTIVATE_OWN("room:deactivate:own"),
    ROOM_READ_BOOKINGS_OWN("room:read-bookings:own"),

    //ANY
    ROOM_READ_ANY("room:read:any"),
    ROOM_CREATE_ANY("room:create:any"),
    ROOM_UPDATE_ANY("room:update:any"),
    ROOM_DELETE_ANY("room:delete:any"),
    ROOM_RESTORE_ANY("room:restore:any"),
    ROOM_ACTIVATE_ANY("room:activate:any"),
    ROOM_DEACTIVATE_ANY("room:deactivate:any"),
    ROOM_READ_BOOKINGS_ANY("room:read-bookings:any"),

    /* --- Bed --- */

    //OWN
    BED_READ_OWN("bed:read:own"),
    BED_CREATE_OWN("bed:create:own"),
    BED_UPDATE_OWN("bed:update:own"),
    BED_DELETE_OWN("bed:delete:own"),
    BED_RESTORE_OWN("bed:restore:own"),

    //ANY
    BED_READ_ANY("bed:read:any"),
    BED_CREATE_ANY("bed:create:any"),
    BED_UPDATE_ANY("bed:update:any"),
    BED_DELETE_ANY("bed:delete:any"),
    BED_RESTORE_ANY("bed:restore:any"),

    /* --- Booking --- */

    //OWN
    BOOKING_READ_OWN("booking:read:own"),
    BOOKING_CREATE_OWN("booking:create:own"),
    BOOKING_UPDATE_OWN("booking:update:own"),
    BOOKING_DELETE_OWN("booking:delete:own"),
    BOOKING_RESTORE_OWN("booking:restore:own"),
    BOOKING_CHANGE_DATES_OWN("booking:change-dates:own"),
    BOOKING_CONFIRM_PAYMENT_OWN("booking:confirm-payment:own"),
    BOOKING_CHECK_IN_OWN("booking:check-in:own"),
    BOOKING_CHECK_OUT_OWN("booking:check-out:own"),
    BOOKING_CANCEL_OWN("booking:cancel:own"),
    BOOKING_REJECT_OWN("booking:reject:own"),

    //ANY
    BOOKING_READ_ANY("booking:read:any"),
    BOOKING_CREATE_ANY("booking:create:any"),
    BOOKING_UPDATE_ANY("booking:update:any"),
    BOOKING_DELETE_ANY("booking:delete:any"),
    BOOKING_RESTORE_ANY("booking:restore:any"),
    BOOKING_CHANGE_DATES_ANY("booking:change-dates:any"),
    BOOKING_CONFIRM_PAYMENT_ANY("booking:confirm-payment:any"),
    BOOKING_CHECK_IN_ANY("booking:check-in:any"),
    BOOKING_CHECK_OUT_ANY("booking:check-out:any"),
    BOOKING_CANCEL_ANY("booking:cancel:any"),
    BOOKING_REJECT_ANY("booking:reject:any"),

    /* --- User --- */

    //ANY
    USER_READ_OWN("user:read:own"),
    USER_UPDATE_OWN("user:update:own"),
    USER_DELETE_OWN("user:delete:own"),
    USER_RESTORE_OWN("user:restore:own"),
    USER_ACTIVATE_OWN("user:activate:own"),
    USER_DEACTIVATE_OWN("user:deactivate:own"),

    //ANY
    USER_READ_ANY("user:read:any"),
    USER_UPDATE_ANY("user:update:any"),
    USER_DELETE_ANY("user:delete:any"),
    USER_RESTORE_ANY("user:restore:any"),
    USER_ACTIVATE_ANY("user:activate:any"),
    USER_DEACTIVATE_ANY("user:deactivate:any");

    @Getter
    private final String permission;
}
