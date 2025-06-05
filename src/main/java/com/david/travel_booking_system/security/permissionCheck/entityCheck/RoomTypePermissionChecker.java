package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.PropertyService;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class RoomTypePermissionChecker extends AbstractPermissionChecker {

    private final RoomTypeService roomTypeService;
    private final PropertyService propertyService;

    @Autowired
    public RoomTypePermissionChecker(PropertyService propertyService, RoomTypeService roomTypeService) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
    }

    public boolean canCreate(Authentication auth, Integer propertyId) {
        return hasPerm(auth, ROOM_TYPE_CREATE)
                && propertyService.isOwner(propertyId, auth.getName());
    }

    public boolean canReadAny(Authentication auth) {
        return true;
    }

    public boolean canRead(Authentication auth, Integer roomTypeId) {
        return true;
    }

    public boolean canUpdate(Authentication auth, Integer roomTypeId) {
        return canOwnOrAny(
                auth,
                ROOM_TYPE_UPDATE_OWN,
                roomTypeId,
                (a, id) -> roomTypeService.isOwner(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer roomTypeId) {
        return canOwnOrAny(
                auth,
                ROOM_TYPE_DELETE_OWN,
                roomTypeId,
                (a, id) -> roomTypeService.isOwner(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer roomTypeId) {
        return canOwnOrAny(
                auth,
                ROOM_TYPE_RESTORE_OWN,
                roomTypeId,
                (a, id) -> roomTypeService.isOwner(id, a.getName())
        );
    }
}
