package com.david.travel_booking_system.service;

import com.david.travel_booking_system.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingValidationService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingValidationService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /* Repo methods ------------------------------------------------------------------------------------------------- */

    public boolean existsBookingsForProperty(Integer propertyId) {
        return bookingRepository.existsBookingsForProperty(propertyId);
    }

    public boolean existsOngoingBookingsForProperty(Integer propertyId) {
        return bookingRepository.existsOngoingBookingsForProperty(propertyId);
    }

    public boolean existsBookingsForRoomType(Integer roomTypeId) {
        return bookingRepository.existsBookingsForRoomType(roomTypeId);
    }

    public boolean existsOngoingBookingsForRoomType(Integer roomTypeId) {
        return bookingRepository.existsOngoingBookingsForRoomType(roomTypeId);
    }

    public boolean existsBookingsForRoom(Integer roomId) {
        return bookingRepository.existsBookingsForRoom(roomId);
    }

    public boolean existsOngoingBookingsForRoom(Integer roomId) {
        return bookingRepository.existsOngoingBookingsForRoom(roomId);
    }

    public boolean existsBookingsForBed(Integer bedId) {
        return bookingRepository.existsBookingsForBed(bedId);
    }

    public boolean existsOngoingBookingsForBed(Integer bedId) {
        return bookingRepository.existsOngoingBookingsForBed(bedId);
    }

    public boolean existsBookingsForUser(Integer userId) {
        return bookingRepository.existsBookingsForUser(userId);
    }

    public boolean existsOngoingBookingsForUser(Integer userId) {
        return bookingRepository.existsOngoingBookingsForUser(userId);
    }
}
