package com.david.travel_booking_system.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER(buildUserPerms()),
    ROLE_HOST(buildHostPerms()),
    ROLE_MANAGER(buildManagerPerms()),
    ROLE_ADMIN(buildAdminPerms());

    @Getter
    private final Set<Permission> permissions;

    private static Set<Permission> buildUserPerms() {
        return EnumSet.of(
                // Property
                Permission.PROPERTY_READ_ANY,

                // Room Type
                Permission.ROOM_TYPE_READ_ANY,

                // Room
                Permission.ROOM_READ_ANY,

                // Bed
                Permission.BED_READ_ANY,

                // Booking
                Permission.BOOKING_CREATE,
                Permission.BOOKING_READ_OWN,
                Permission.BOOKING_UPDATE_OWN,
                Permission.BOOKING_DELETE_OWN,
                Permission.BOOKING_RESTORE_OWN,
                Permission.BOOKING_CHANGE_DATES_OWN,
                Permission.BOOKING_CONFIRM_PAYMENT_OWN,
                Permission.BOOKING_CANCEL_OWN,

                // User
                Permission.USER_READ_OWN,
                Permission.USER_UPDATE_OWN,
                Permission.USER_DELETE_OWN,
                Permission.USER_RESTORE_OWN,
                Permission.USER_ACTIVATE_OWN,
                Permission.USER_DEACTIVATE_OWN
        );
    }

    private static Set<Permission> buildHostPerms() {
        // Start with user permissions
        Set<Permission> perms = EnumSet.copyOf(buildUserPerms());

        // Add host-specific permissions
        perms.addAll(EnumSet.of(
                // Property
                Permission.PROPERTY_CREATE,
                Permission.PROPERTY_READ_OWN,
                Permission.PROPERTY_UPDATE_OWN,
                Permission.PROPERTY_DELETE_OWN,
                Permission.PROPERTY_RESTORE_OWN,
                Permission.PROPERTY_ACTIVATE_OWN,
                Permission.PROPERTY_DEACTIVATE_OWN,
                Permission.PROPERTY_READ_BOOKINGS_OWN,

                // Room Type
                Permission.ROOM_TYPE_CREATE,
                Permission.ROOM_TYPE_READ_OWN,
                Permission.ROOM_TYPE_UPDATE_OWN,
                Permission.ROOM_TYPE_DELETE_OWN,
                Permission.ROOM_TYPE_RESTORE_OWN,

                // Room
                Permission.ROOM_CREATE,
                Permission.ROOM_READ_OWN,
                Permission.ROOM_UPDATE_OWN,
                Permission.ROOM_DELETE_OWN,
                Permission.ROOM_RESTORE_OWN,
                Permission.ROOM_ACTIVATE_OWN,
                Permission.ROOM_DEACTIVATE_OWN,
                Permission.ROOM_READ_BOOKINGS_OWN,

                // Bed
                Permission.BED_CREATE,
                Permission.BED_READ_OWN,
                Permission.BED_UPDATE_OWN,
                Permission.BED_DELETE_OWN,
                Permission.BED_RESTORE_OWN,

                // Booking
                Permission.BOOKING_CHECK_IN_OWN,
                Permission.BOOKING_CHECK_OUT_OWN,
                Permission.BOOKING_REJECT_OWN
        ));
        return perms;
    }

    private static Set<Permission> buildManagerPerms() {
        // Start with user permissions
        Set<Permission> perms = EnumSet.copyOf(buildUserPerms());

        // Add manager-specific permissions
        perms.addAll(EnumSet.of(
                // Property
                Permission.PROPERTY_UPDATE_ANY,
                Permission.PROPERTY_RESTORE_ANY,
                Permission.PROPERTY_ACTIVATE_ANY,
                Permission.PROPERTY_DEACTIVATE_ANY,
                Permission.PROPERTY_READ_BOOKINGS_ANY,

                // Room Type
                Permission.ROOM_TYPE_UPDATE_ANY,
                Permission.ROOM_TYPE_RESTORE_ANY,

                // Room
                Permission.ROOM_UPDATE_ANY,
                Permission.ROOM_RESTORE_ANY,
                Permission.ROOM_ACTIVATE_ANY,
                Permission.ROOM_DEACTIVATE_ANY,
                Permission.ROOM_READ_BOOKINGS_ANY,

                // Bed
                Permission.BED_UPDATE_ANY,
                Permission.BED_RESTORE_ANY,

                // Booking
                Permission.BOOKING_READ_ANY,
                Permission.BOOKING_UPDATE_ANY,
                Permission.BOOKING_RESTORE_ANY,
                Permission.BOOKING_CONFIRM_PAYMENT_ANY,
                Permission.BOOKING_CHECK_IN_ANY,
                Permission.BOOKING_CHECK_OUT_ANY,
                Permission.BOOKING_REJECT_ANY,

                // User
                Permission.USER_READ_ANY,
                Permission.USER_UPDATE_ANY,
                Permission.USER_RESTORE_ANY,
                Permission.USER_ACTIVATE_ANY,
                Permission.USER_DEACTIVATE_ANY
        ));
        return perms;
    }

    private static Set<Permission> buildAdminPerms() {
        // Admin has all permissions
        return EnumSet.allOf(Permission.class);
    }
}
