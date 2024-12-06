package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    boolean existsByRoom_IdAndCheckInDateBeforeAndCheckOutDateAfter(Integer roomId, LocalDate checkOutDate, LocalDate checkInDate);
}
