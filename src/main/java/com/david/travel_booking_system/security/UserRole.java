package com.david.travel_booking_system.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
                Permission.PROPERTY_READ,
                Permission.ROOM_TYPE_READ,
                Permission.ROOM_READ,
                Permission.BED_READ,
                Permission.BOOKING_READ,
                Permission.BOOKING_CREATE,
                Permission.BOOKING_UPDATE,
                Permission.BOOKING_DELETE,
                Permission.BOOKING_RESTORE,
                Permission.BOOKING_CHANGE_DATES,
                Permission.BOOKING_CONFIRM_PAYMENT,
                Permission.BOOKING_CANCEL,
                Permission.USER_READ,
                Permission.USER_UPDATE,
                Permission.USER_DELETE,
                Permission.USER_RESTORE,
                Permission.USER_ACTIVATE,
                Permission.USER_DEACTIVATE
        );
    }

    private static Set<Permission> buildHostPerms() {
        // Start with user permissions
        Set<Permission> perms = EnumSet.copyOf(buildUserPerms());

        // Add host-specific permissions
        perms.addAll(EnumSet.of(
                Permission.PROPERTY_CREATE,
                Permission.PROPERTY_UPDATE,
                Permission.PROPERTY_DELETE,
                Permission.PROPERTY_RESTORE,
                Permission.PROPERTY_ACTIVATE,
                Permission.PROPERTY_DEACTIVATE,
                Permission.PROPERTY_READ_BOOKINGS,
                Permission.ROOM_TYPE_CREATE,
                Permission.ROOM_TYPE_UPDATE,
                Permission.ROOM_TYPE_DELETE,
                Permission.ROOM_TYPE_RESTORE,
                Permission.ROOM_CREATE,
                Permission.ROOM_UPDATE,
                Permission.ROOM_DELETE,
                Permission.ROOM_RESTORE,
                Permission.ROOM_ACTIVATE,
                Permission.ROOM_DEACTIVATE,
                Permission.ROOM_READ_BOOKINGS,
                Permission.BED_CREATE,
                Permission.BED_UPDATE,
                Permission.BED_DELETE,
                Permission.BED_RESTORE,
                Permission.BOOKING_CHECK_IN,
                Permission.BOOKING_CHECK_OUT,
                Permission.BOOKING_REJECT
        ));
        return perms;
    }

    private static Set<Permission> buildManagerPerms() {
        // Start with user permissions
        Set<Permission> perms = EnumSet.copyOf(buildUserPerms());

        // Add manager-specific permissions
        perms.addAll(EnumSet.of(
                Permission.PROPERTY_UPDATE,
                Permission.PROPERTY_RESTORE,
                Permission.PROPERTY_ACTIVATE,
                Permission.PROPERTY_DEACTIVATE,
                Permission.PROPERTY_READ_BOOKINGS,
                Permission.ROOM_TYPE_UPDATE,
                Permission.ROOM_TYPE_RESTORE,
                Permission.ROOM_UPDATE,
                Permission.ROOM_RESTORE,
                Permission.ROOM_ACTIVATE,
                Permission.ROOM_DEACTIVATE,
                Permission.ROOM_READ_BOOKINGS,
                Permission.BED_UPDATE,
                Permission.BED_RESTORE,
                Permission.BOOKING_CHECK_IN,
                Permission.BOOKING_CHECK_OUT,
                Permission.BOOKING_REJECT,

        ));
        return perms;
    }

    private static Set<Permission> buildAdminPerms() {
        // Admin has all permissions
        return EnumSet.allOf(Permission.class);
    }
}
