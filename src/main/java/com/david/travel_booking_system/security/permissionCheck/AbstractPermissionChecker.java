package com.david.travel_booking_system.security.permissionCheck;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPermissionChecker<T> implements EntityPermissionChecker<T>{
    private final RoleHierarchy roleHierarchy;

    public AbstractPermissionChecker(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    // Extracts the authority strings from the Authentication
    protected Set<String> auths(Authentication auth) {
        Collection<? extends GrantedAuthority> all =
                roleHierarchy.getReachableGrantedAuthorities(auth.getAuthorities());

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    // Checks for a specific permission string (e.g. "property:update")
    protected boolean hasPerm(Set<String> auths, String perm) {
        return auths.contains(perm);
    }

    // Checks if the user has any one of the given roles
    protected boolean hasAnyRole(Set<String> auths, String... roles) {
        return Arrays.stream(roles).anyMatch(auths::contains);
    }
}
