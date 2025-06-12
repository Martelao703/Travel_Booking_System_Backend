package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.BookingService;
import com.david.travel_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class BookingPermissionChecker extends AbstractPermissionChecker {

    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public BookingPermissionChecker(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    public boolean canCreate(Authentication auth, Integer userId) {
        return canOwnOrAny(
                auth,
                BOOKING_CREATE_OWN,
                userId,
                // Only the authenticated user can create a booking for themselves if they are not an admin
                (a, id) -> userService.isSelf(id, a.getName())
        );
    }

    public boolean canReadAny(Authentication auth) {
        return hasPerm(auth, BOOKING_READ_ANY);
    }

    public boolean canRead(Authentication auth, Integer bookingId) {
        if (bookingService.isBookingOnOwnedProperty(bookingId, auth.getName())) {
            return true;
        }

        return canOwnOrAny(
                auth,
                BOOKING_READ_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canUpdate(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_UPDATE_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canDelete(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_DELETE_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canRestore(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_RESTORE_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canChangeDates(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_CHANGE_DATES_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canConfirmPayment(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_CONFIRM_PAYMENT_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canCheckIn(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_CHECK_IN_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canCheckOut(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_CHECK_OUT_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canCancel(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_CANCEL_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }

    public boolean canReject(Authentication auth, Integer bookingId) {
        return canOwnOrAny(
                auth,
                BOOKING_REJECT_OWN,
                bookingId,
                (a, id) -> bookingService.isOwner(id, a.getName())
        );
    }
}
