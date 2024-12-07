package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    boolean existsByRoom_IdAndCheckInDateBeforeAndCheckOutDateAfter(Integer roomId, LocalDate checkOutDate, LocalDate checkInDate);

    // Check if any room in a property has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "JOIN r.roomType rt " +
            "WHERE rt.property.id = :propertyId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' ")
    boolean existsBookingsForProperty(@Param("propertyId") Integer propertyId);

    // Check if any room of a room type has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "WHERE r.roomType.id = :roomTypeId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' ")
    boolean existsBookingsForRoomType(@Param("roomTypeId") Integer roomTypeId);

    // Check if a room has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' ")
    boolean existsBookingsForRoom(@Param("roomId") Integer roomId);

    // Check if any room of all room types associated with a bed has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "JOIN r.roomType rt " +
            "JOIN rt.beds bed " +
            "WHERE bed.id = :bedId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' ")
    boolean existsBookingsForBed(Integer bedId);
}
