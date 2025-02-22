package com.david.travel_booking_system.enumsAndSets;

public enum BookingStatus {
    PENDING,    // the user has requested a booking, but has not confirmed or paid yet
    CONFIRMED,  // the user has confirmed and paid for the booking, but has not checked in yet
    ONGOING,    // the user in currently in the room
    COMPLETED,  // the user has checked out
    EXPIRED,    // the user has not confirmed or paid for the booking within the time limit
    CANCELLED,  // the user has cancelled the booking
    REJECTED,   // the user's booking has been rejected
    NO_SHOW     // the user has not checked in within the time limit
}
