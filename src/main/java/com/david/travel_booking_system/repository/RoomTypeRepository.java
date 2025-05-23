package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer>, JpaSpecificationExecutor<RoomType> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    /* By Property */

    @Modifying
    @Query("""
            UPDATE RoomType rt SET rt.deleted = true
            WHERE rt.property.id = :propertyId AND rt.deleted = false
            """)
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("""
            UPDATE RoomType rt SET rt.deleted = false
            WHERE rt.property.id = :propertyId AND rt.deleted = true
            """)
    void restoreByPropertyId(@Param("propertyId") Integer propertyId);

    /* By Owner (User) */

    @Modifying
    @Query("""
            UPDATE RoomType rt  SET rt.deleted = true
            WHERE rt.property.owner.id = :ownerId AND rt.deleted = false
            """)
    void softDeleteByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE RoomType rt SET rt.deleted = false
            WHERE rt.property.owner.id = :ownerId AND rt.deleted = true
            """)
    void restoreByOwnerId(@Param("ownerId") Integer ownerId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
