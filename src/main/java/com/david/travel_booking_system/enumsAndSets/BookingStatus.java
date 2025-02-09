package com.david.travel_booking_system.enumsAndSets;

public enum BookingStatus {
    CONFIRMED,  // the user has confirmed and paid for the booking, but has not checked in yet
    ONGOING,    // the user in currently in the room
    PENDING,    // the user has requested a booking, but has not confirmed or paid yet
    CANCELLED,  // the user has cancelled the booking
    COMPLETED,  // the user has checked out
    REJECTED,   // the user's booking has been rejected
    EXPIRED,    // the user has not confirmed or paid for the booking within the time limit
    NO_SHOW     // the user has not checked in within the time limit
}
