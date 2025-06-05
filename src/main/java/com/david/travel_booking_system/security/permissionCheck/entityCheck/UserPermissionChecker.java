package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class UserPermissionChecker extends AbstractPermissionChecker {

    private final UserService userService;

    @Autowired
    public UserPermissionChecker(UserService userService) {
        this.userService = userService;
    }

    public boolean canReadAny(Authentication auth) {
        return hasPerm(auth, USER_READ_ANY);
    }

    public boolean canRead(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_READ_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canUpdate(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_UPDATE_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_DELETE_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_RESTORE_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canActivate(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_ACTIVATE_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canDeactivate(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                USER_DEACTIVATE_OWN,
                userId,
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }
}
