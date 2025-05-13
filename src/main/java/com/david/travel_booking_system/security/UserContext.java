package com.david.travel_booking_system.security;

import com.david.travel_booking_system.model.User;
import lombok.Getter;

@Getter
public class UserContext {

    private final User user;

    public UserContext(User user) {
        this.user = user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Integer getUserId() {
        return user.getId();
    }

    public boolean hasRole(UserRole role) {
        return user.getRoles().contains(role);
    }

    public boolean isHost() {
        return hasRole(UserRole.ROLE_HOST);
    }

    public boolean isManager() {
        return hasRole(UserRole.ROLE_MANAGER);
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ROLE_ADMIN);
    }

    public boolean isLoggedIn() {
        return user != null;
    }
}
