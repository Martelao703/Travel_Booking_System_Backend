package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Find bookings that are pending and past expiry threshold
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.status = :status " +
            "AND b.createdAt < :expiryThreshold")
    List<Booking> findExpiredBookings(
            @Param("status") BookingStatus status, @Param("expiryThreshold") LocalDateTime expiryThreshold
    );

    // Find bookings that are confirmed and past check-in time without check-in
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.status = :status " +
            "AND b.plannedCheckInDateTime < :time " +
            "AND b.actualCheckInDateTime IS NULL")
    List<Booking> findConfirmedBookingsPastCheckInTimeWithoutCheckIn(
            @Param("status") BookingStatus status, @Param("time") LocalDateTime time
    );

    // Check if booking dates overlap with other booking dates
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.plannedCheckInDateTime < :checkOut " +
            "AND b.plannedCheckOutDateTime > :checkIn")
    boolean areDatesOverlappingOtherBookings(
            @Param("roomId") Integer roomId, @Param("checkOut") LocalDateTime checkOut, @Param("checkIn") LocalDateTime checkIn
    );

    // Check if booking dates overlap with other booking dates, excluding a booking
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.plannedCheckInDateTime < :checkOut " +
            "AND b.plannedCheckOutDateTime > :checkIn " +
            "AND b.id <> :bookingId " +
            "AND b.status IN :validStatuses")
    boolean areDatesOverlappingOtherBookingIdCheck(
            @Param("roomId") Integer roomId, @Param("checkOut") LocalDateTime checkOut, @Param("checkIn") LocalDateTime checkIn,
            @Param("bookingId") Integer bookingId, @Param("validStatuses") List<BookingStatus> validStatuses);

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
