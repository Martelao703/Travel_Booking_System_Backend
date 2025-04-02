package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Bed;
import com.david.travel_booking_system.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer>, JpaSpecificationExecutor<Bed> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    @Modifying
    @Query(" UPDATE Bed b SET b.deleted = true WHERE b.id = :id ")
    void softDeleteById(Integer id);

    @Modifying
    @Query("UPDATE Bed b SET b.deleted = true WHERE b.roomType.id = :roomTypeId AND b.deleted = false ")
    void softDeleteByRoomTypeId(Integer roomTypeId);

    @Modifying
    @Query("UPDATE Bed b " +
            "SET b.deleted = true " +
            "WHERE b.roomType IN (SELECT rt " +
            "                     FROM RoomType rt " +
            "                     WHERE rt.property.id = :propertyId" +
            "                     AND rt.deleted = false) " +
            "AND b.deleted = false")
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
