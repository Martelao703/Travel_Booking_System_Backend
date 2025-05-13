package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PropertyPermissionCheck extends AbstractPermissionChecker<Property> {

    @Override
    public boolean canCreate(Authentication auth, Property property) {
        Set<String> auths = auths(auth);
        return hasPerm(auths, "property:create")
                && hasAnyRole(auths, "ROLE_HOST", "ROLE_MANAGER", "ROLE_ADMIN");
    }

    @Override
    public boolean canRead(Authentication auth, Property property) {
        return false;
    }

    @Override
    public boolean canUpdate(Authentication auth, Property property) {
        Set<String> auths = auths(auth);

        if (!hasPerm(auths, "property:update")) return false;
        if (hasAnyRole(auths, "ROLE_MANAGER", "ROLE_ADMIN")) return true;

        return property.getOwnerUsername().equals(auth.getName());
    }

    @Override
    public boolean canDelete(Authentication auth, Property property) {
        return false;
    }

    @Override
    public boolean canRestore(Authentication auth, Property property) {
        return false;
    }
}
