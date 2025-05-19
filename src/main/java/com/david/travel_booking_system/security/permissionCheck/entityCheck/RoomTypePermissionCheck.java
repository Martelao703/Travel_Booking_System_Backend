package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RoomTypePermissionCheck extends AbstractPermissionChecker {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypePermissionCheck(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    public boolean canCreate(Authentication auth) {
        return hasPerm(auth, "roomType:create");
    }

    private boolean isOwnerOrManager(Authentication auth, Integer roomTypeId, String permission) {
        if (!hasPerm(auth, permission)) return false;
        if (hasAnyRole(auth, "ROLE_MANAGER")) return true;

        return roomTypeService.isOwner(roomTypeId, auth.getName());
    }
}
