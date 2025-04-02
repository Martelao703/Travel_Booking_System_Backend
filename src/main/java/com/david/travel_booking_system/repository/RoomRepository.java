package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    @Modifying
    @Query(" UPDATE Room r SET r.deleted = true WHERE r.id = :id ")
    void softDeleteById(Integer id);

    @Modifying
    @Query("UPDATE Room r SET r.deleted = true WHERE r.roomType.id = :roomTypeId AND r.deleted = false ")
    void softDeleteByRoomTypeId(Integer roomTypeId);

    @Modifying
    @Query("UPDATE Room r " +
            "SET r.deleted = true, r.active = false " +
            "WHERE r.roomType IN (SELECT rt " +
            "                     FROM RoomType rt " +
            "                     WHERE rt.property.id = :propertyId" +
            "                     AND rt.deleted = false) " +
            "AND r.deleted = false")
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
