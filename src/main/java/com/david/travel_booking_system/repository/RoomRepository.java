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

    /* By RoomType */

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = true, r.active = false
            WHERE r.roomType.id = :roomTypeId AND r.deleted = false
            """)
    void softDeleteByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = false
            WHERE r.roomType.id = :roomTypeId AND r.deleted = true
            """)
    void restoreByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = true
            WHERE r.roomType.id = :roomTypeId AND r.deleted = false
            """)
    void activateByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = false
            WHERE r.roomType.id = :roomTypeId AND r.deleted = false
            """)
    void deactivateByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    /* By Property */

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = true, r.active = false
            WHERE r.roomType.property.id = :propertyId AND r.deleted = false
            """)
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = false
            WHERE r.roomType.property.id = :propertyId AND r.deleted = true
            """)
    void restoreByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = true
            WHERE r.roomType.property.id = :propertyId AND r.deleted = false
            """)
    void activateByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = false
            WHERE r.roomType.property.id = :propertyId AND r.deleted = false
            """)
    void deactivateByPropertyId(@Param("propertyId") Integer propertyId);

    /* By Owner (User) */

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = true, r.active = false
            WHERE r.roomType.property.owner.id = :ownerId AND r.deleted = false
            """)
    void softDeleteByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.deleted = false
            WHERE r.roomType.property.owner.id = :ownerId AND r.deleted = true
            """)
    void restoreByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = true
            WHERE r.roomType.property.owner.id = :ownerId AND r.deleted = false
            """)
    void activateByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Room r SET r.active = false
            WHERE r.roomType.property.owner.id = :ownerId AND r.deleted = false
            """)
    void deactivateByOwnerId(@Param("ownerId") Integer ownerId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
