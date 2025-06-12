package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.PropertyService;
import com.david.travel_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class PropertyPermissionChecker extends AbstractPermissionChecker {

    private final PropertyService propertyService;
    private final UserService userService;

    @Autowired
    public PropertyPermissionChecker(PropertyService propertyService, UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    public boolean canCreate(Authentication auth, Integer ownerId) {
        return canOwnOrAny(
                auth,
                PROPERTY_CREATE_OWN,
                ownerId,
                // Only the authenticated user can create a property for themselves if they are not an admin
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canReadAny(Authentication auth) {
        return true;
    }

    public boolean canRead(Authentication auth, Integer propertyId) {
        return true;
    }

    public boolean canUpdate(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_UPDATE_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_DELETE_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_RESTORE_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }

    public boolean canActivate(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_ACTIVATE_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }

    public boolean canDeactivate(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_DEACTIVATE_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }

    public boolean canReadBookings(Authentication auth, Integer propertyId) {
        return canOwnOrAny(
                auth,
                PROPERTY_READ_BOOKINGS_OWN,
                propertyId,
                (a, id) -> propertyService.isOwner(id, a.getName())
        );
    }
}
