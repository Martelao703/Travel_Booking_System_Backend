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
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' OR b.status = 'ONGOING' ")
    boolean existsBookingsForProperty(@Param("propertyId") Integer propertyId);

    // Check if any room in a property has ongoing bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "JOIN r.roomType rt " +
            "WHERE rt.property.id = :propertyId " +
            "AND b.status = 'ONGOING' ")
    boolean existsOngoingBookingsForProperty(@Param("propertyId") Integer propertyId);

    // Check if any room of a room type has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "WHERE r.roomType.id = :roomTypeId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' OR b.status = 'ONGOING' ")
    boolean existsBookingsForRoomType(@Param("roomTypeId") Integer roomTypeId);

    // Cehck if any room of a room type has ongoing bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "WHERE r.roomType.id = :roomTypeId " +
            "AND b.status = 'ONGOING' ")
    boolean existsOngoingBookingsForRoomType(@Param("roomTypeId") Integer roomTypeId);

    // Check if a room has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' OR b.status = 'ONGOING' ")
    boolean existsBookingsForRoom(@Param("roomId") Integer roomId);

    // Check if a room has ongoing bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.status = 'ONGOING' ")
    boolean existsOngoingBookingsForRoom(@Param("roomId") Integer roomId);

    // Check if any room of all room types associated with a bed has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "JOIN r.roomType rt " +
            "JOIN rt.beds bed " +
            "WHERE bed.id = :bedId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' OR b.status = 'ONGOING' ")
    boolean existsBookingsForBed(Integer bedId);

    // Check if any room of all room types associated with a bed has ongoing bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "JOIN b.room r " +
            "JOIN r.roomType rt " +
            "JOIN rt.beds bed " +
            "WHERE bed.id = :bedId " +
            "AND b.status = 'ONGOING' ")
    boolean existsOngoingBookingsForBed(Integer bedId);

    // Check if a user has bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.user.id = :userId " +
            "AND b.status = 'CONFIRMED' OR b.status = 'PENDING' OR b.status = 'ONGOING' ")
    boolean existsBookingsForUser(@Param("userId") Integer userId);

    // Check if a user has ongoing bookings
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.user.id = :userId " +
            "AND b.status = 'ONGOING' ")
    boolean existsOngoingBookingsForUser(@Param("userId") Integer userId);
}
