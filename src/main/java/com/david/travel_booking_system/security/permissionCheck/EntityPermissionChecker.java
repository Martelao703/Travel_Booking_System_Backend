package com.david.travel_booking_system.security.permissionCheck;

import org.springframework.security.core.Authentication;

/**
 * Interface that defines the basic CRUD permission checks for an entity
 *
 * @param <T> the type of entity
 */
public interface EntityPermissionChecker<T> {
    boolean canCreate(Authentication auth, T entity);
    boolean canRead  (Authentication auth, T entity);
    boolean canUpdate(Authentication auth, T entity);
    boolean canDelete(Authentication auth, T entity);
    boolean canRestore(Authentication auth, T entity);
}
