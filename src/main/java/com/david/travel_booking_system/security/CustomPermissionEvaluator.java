package com.david.travel_booking_system.security;

import com.david.travel_booking_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;
    private final BedService bedService;
    private final BookingService bookingService;

    @Autowired
    public CustomPermissionEvaluator(PropertyService propertyService, RoomTypeService roomTypeService,
                                     RoomService roomService, BedService bedService, BookingService bookingService) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
        this.bedService = bedService;
        this.bookingService = bookingService;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        // Domain object security not implemented
        return false;
    }

    // Used by SpEL expressions in @PreAuthorize
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if (auth == null || targetId == null || targetType == null || permission == null) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UserContext userCtx = new UserContext(userDetails.getUser());
        Integer id = (Integer) targetId;
        String perm = permission.toString();

        // ADMIN bypass
        if (userCtx.isAdmin()) return true;

        return switch (targetType) {
            case "Property" -> checkProperty(id, perm, userCtx);
            case "RoomType" -> checkRoomType(id, perm, userCtx);
            case "Room" -> checkRoom(id, perm, userCtx);
            case "Bed" -> checkBed(id, perm, userCtx);
            case "Booking" -> checkBooking(id, perm, userCtx);
            case "User" -> checkUser(id, perm, userCtx);
            default -> false;
        };
    }

    private boolean checkProperty(Integer id, String perm, UserContext user) {
        boolean isOwner = propertyService.isOwner(id, user.getEmail());

        return switch (perm) {
            case "read" -> true;
            case "read-bookings", "update", "activate", "deactivate", "restore" -> isOwner || user.isManager();
            case "create" -> user.isHost();
            case "delete" -> isOwner;
            default -> false;
        };
    }

    private boolean checkRoomType(Integer id, String perm, UserContext user) {
        // If perm is "create", the ID is the property ID
        if (perm.equals("create")) {
            return propertyService.isOwner(id, user.getEmail());
        }

        // Else, the ID is the room type ID
        boolean isOwner = roomTypeService.isOwner(id, user.getEmail());

        return switch (perm) {
            case "read" -> true;
            case "delete" -> isOwner;
            case "update", "restore" -> isOwner || user.isManager();
            default -> false;
        };
    }

    private boolean checkRoom(Integer id, String perm, UserContext user) {
        // If perm is "create", the ID is the room type ID
        if (perm.equals("create")) {
            return roomTypeService.isOwner(id, user.getEmail());
        }

        // Else, the ID is the room ID
        boolean isOwner = roomService.isOwner(id, user.getEmail());

        return switch (perm) {
            case "read" -> true;
            case "delete" -> isOwner;
            case "read-bookings", "update", "activate", "deactivate", "restore" -> isOwner || user.isManager();
            default -> false;
        };
    }

    private boolean checkBed(Integer id, String perm, UserContext user) {
        // If perm is "create", the ID is the room type ID
        if (perm.equals("create")) {
            return roomTypeService.isOwner(id, user.getEmail());
        }

        // Else, the ID is the bed ID
        boolean isOwner = bedService.isOwner(id, user.getEmail());

        return switch (perm) {
            case "read" -> true;
            case "delete" -> isOwner;
            case "update", "restore" -> isOwner || user.isManager();
            default -> false;
        };
    }

    private boolean checkBooking(Integer id, String perm, UserContext user) {
        boolean isBookingOwner = bookingService.isOwner(id, user.getEmail());
        boolean isBookingOnOwnedProperty = bookingService.isBookingOnOwnedProperty(id, user.getEmail());

        return switch (perm) {
            case "read" -> isBookingOwner || isBookingOnOwnedProperty || user.isManager();
            case "create" -> user.isLoggedIn();
            case "update", "restore", "confirmPayment" -> isBookingOwner || user.isManager();
            case "delete", "cancelBooking", "changeBookingDates" -> isBookingOwner;
            case "checkIn", "checkOut", "rejectBooking" -> isBookingOnOwnedProperty || user.isManager();
            default -> false;
        };
    }

    private boolean checkUser(Integer id, String perm, UserContext user) {
        boolean isSelf = user.getUserId().equals(id);

        return switch (perm) {
            case "read", "update", "activate", "deactivate", "restore" -> isSelf || user.isManager();
            case "delete" -> isSelf;
            default -> false;
        };
    }
}