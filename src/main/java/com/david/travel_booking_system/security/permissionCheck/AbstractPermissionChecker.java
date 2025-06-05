package com.david.travel_booking_system.security.permissionCheck;

import com.david.travel_booking_system.security.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class AbstractPermissionChecker {

    /**
     * Checks if the user either has the ANY variant of the given OWN permission,
     * or has the OWN permission and owns the resource.
     */
    protected boolean canOwnOrAny(Authentication auth, Permission ownPerm, Integer resourceId,
                                  BiFunction<Authentication, Integer, Boolean> isOwner
    ) {
        // derive the ANY variant by swapping suffix
        Permission anyPerm = Permission.valueOf(
                ownPerm.name().replace("_OWN", "_ANY")
        );

        // ANY
        if (hasPerm(auth, anyPerm)) return true;

        // OWN
        return hasPerm(auth, ownPerm)
                && isOwner.apply(auth, resourceId);
    }

    protected boolean hasPerm(Authentication auth, Permission perm) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(perm.getPermission());
        return auth.getAuthorities().contains(authority);
    }
}
