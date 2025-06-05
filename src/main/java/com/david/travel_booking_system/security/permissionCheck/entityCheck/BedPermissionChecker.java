package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.BedService;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class BedPermissionChecker extends AbstractPermissionChecker {

    private final BedService bedService;
    private final RoomTypeService roomTypeService;

    @Autowired
    public BedPermissionChecker(BedService bedService, RoomTypeService roomTypeService) {
        this.bedService = bedService;
        this.roomTypeService = roomTypeService;
    }

    public boolean canCreate(Authentication auth, Integer roomTypeId) {
        return hasPerm(auth, BED_CREATE)
                && roomTypeService.isOwner(roomTypeId, auth.getName());
    }

    public boolean canReadAny(Authentication auth) {
        return true;
    }

    public boolean canRead(Authentication auth, Integer bedId) {
        return true;
    }

    public boolean canUpdate(Authentication auth, Integer bedId) {
        return canOwnOrAny(
                auth,
                BED_UPDATE_OWN,
                bedId,
                (a, id) -> bedService.isOwner(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer bedId) {
        return canOwnOrAny(
                auth,
                BED_DELETE_OWN,
                bedId,
                (a, id) -> bedService.isOwner(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer bedId) {
        return canOwnOrAny(
                auth,
                BED_RESTORE_OWN,
                bedId,
                (a, id) -> bedService.isOwner(id, a.getName())
        );
    }
}
