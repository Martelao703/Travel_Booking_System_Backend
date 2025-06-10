package com.david.travel_booking_system.security.permissionCheck.entityCheck;

import com.david.travel_booking_system.security.permissionCheck.AbstractPermissionChecker;
import com.david.travel_booking_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.david.travel_booking_system.security.Permission.*;

@Component
public class BookingPermissionChecker extends AbstractPermissionChecker {

    private final BookingService bookingService;

    @Autowired
    public BookingPermissionChecker(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public boolean canCreate(Authentication auth) {
        return hasPerm(auth, BOOKING_CREATE);
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
