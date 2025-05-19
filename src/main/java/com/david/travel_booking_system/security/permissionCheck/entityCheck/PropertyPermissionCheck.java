package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PropertyPermissionCheck extends AbstractPermissionChecker {

    private final PropertyService propertyService;

    @Autowired
    public PropertyPermissionCheck(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public boolean canCreate(Authentication auth) {
        return hasPerm(auth, "property:create");
    }

    public boolean canRead(Authentication auth, Integer propertyId) {
        return hasPerm(auth, "property:read");
    }

    public boolean canUpdate(Authentication auth, Integer propertyId) {
        return isOwnerOrManager(auth, propertyId, "property:update");
    }

    public boolean canDelete(Authentication auth, Integer propertyId) {
        return hasPerm(auth, "property:delete") && propertyService.isOwner(propertyId, auth.getName());
    }

    public boolean canRestore(Authentication auth, Integer propertyId) {
        return isOwnerOrManager(auth, propertyId, "property:restore");
    }

    public boolean canActivate(Authentication auth, Integer propertyId) {
        return isOwnerOrManager(auth, propertyId, "property:activate");
    }

    public boolean canDeactivate(Authentication auth, Integer propertyId) {
        return isOwnerOrManager(auth, propertyId, "property:deactivate");
    }

    private boolean isOwnerOrManager(Authentication auth, Integer propertyId, String permission) {
        if (!hasPerm(auth, permission)) return false;
        if (hasAnyRole(auth, "ROLE_MANAGER")) return true;

        return propertyService.isOwner(propertyId, auth.getName());
    }
}
