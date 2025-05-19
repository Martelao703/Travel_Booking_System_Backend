package com.david.travel_booking_system.security.permissionCheck;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPermissionChecker {

    // Extracts the authority strings from the Authentication
    private Set<String> authorities(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    // Checks for a specific permission string (e.g. "property:update")
    protected boolean hasPerm(Authentication auth, String perm) {
        return authorities(auth).contains(perm);
    }

    // Checks if the user has any one of the given roles
    protected boolean hasAnyRole(Authentication auth, String... roles) {
        Set<String> auths = authorities(auth);
        return Arrays.stream(roles).anyMatch(auths::contains);
    }
}
