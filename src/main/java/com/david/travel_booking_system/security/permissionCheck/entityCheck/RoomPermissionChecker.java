package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.RoomService;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class RoomPermissionChecker extends AbstractPermissionChecker {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomPermissionChecker(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    public boolean canCreate(Authentication auth, Integer roomTypeId) {
        return hasPerm(auth, ROOM_CREATE)
                && roomTypeService.isOwner(roomTypeId, auth.getName());
    }

    public boolean canReadAny(Authentication auth) {
        return true;
    }

    public boolean canRead(Authentication auth, Integer roomId) {
        return true;
    }

    public boolean canUpdate(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_UPDATE_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_DELETE_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_RESTORE_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }

    public boolean canActivate(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_ACTIVATE_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }

    public boolean canDeactivate(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_DEACTIVATE_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }

    public boolean canReadBookings(Authentication auth, Integer roomId) {
        return canOwnOrAny(
                auth,
                ROOM_READ_BOOKINGS_OWN,
                roomId,
                (a, id) -> roomService.isOwner(id, a.getName())
        );
    }
}
