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

    /* By RoomType */

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = true
            WHERE b.roomType.id = :roomTypeId AND b.deleted = false
            """)
    void softDeleteByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = false
            WHERE b.roomType.id = :roomTypeId AND b.deleted = true
            """)
    void restoreByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /* By Property */

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = true
            WHERE b.roomType.property.id = :propertyId AND b.deleted = false
            """)
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = false
            WHERE b.roomType.property.id = :propertyId AND b.deleted = true
            """)
    void restoreByPropertyId(@Param("propertyId") Integer propertyId);

    /* By Owner (User) */

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = true
            WHERE b.roomType.property.owner.id = :ownerId AND b.deleted = false
            """)
    void softDeleteByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Bed b SET b.deleted = false
            WHERE b.roomType.property.owner.id = :ownerId AND b.deleted = true
            """)
    void restoreByOwnerId(@Param("ownerId") Integer ownerId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
