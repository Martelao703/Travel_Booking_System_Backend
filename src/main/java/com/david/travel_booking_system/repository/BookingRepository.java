package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    @Modifying
    @Query(" UPDATE Booking b SET b.deleted = true WHERE b.id = :id ")
    void softDeleteById(Integer id);

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    // Find Bookings that are pending and past expiry threshold
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.createdAt < :expiryThreshold")
    List<Booking> findExpiredBookings(
            @Param("status") BookingStatus status, @Param("expiryThreshold") LocalDateTime expiryThreshold
    );

    // Find Bookings that are confirmed and past check-in time without check-in
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.status = :status " +
            "AND b.plannedCheckInDateTime < :time " +
            "AND b.actualCheckInDateTime IS NULL")
    List<Booking> findConfirmedBookingsPastCheckInTimeWithoutCheckIn(
            @Param("status") BookingStatus status, @Param("time") LocalDateTime time
    );

    // Check if Booking dates overlap with other Booking dates
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.plannedCheckInDateTime < :checkOut " +
            "AND b.plannedCheckOutDateTime > :checkIn " +
            "AND b.status IN :validStatuses")
    boolean areDatesOverlappingOtherBookings(
            @Param("roomId") Integer roomId, @Param("checkOut") LocalDateTime checkOut, @Param("checkIn") LocalDateTime checkIn,
            @Param("validStatuses") List<BookingStatus> validStatuses
    );

    // Check if Booking dates overlap with other Booking dates, excluding a booking
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
}
