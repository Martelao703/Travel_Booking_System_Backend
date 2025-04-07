package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer>, JpaSpecificationExecutor<RoomType> {
    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    @Modifying
    @Query("UPDATE RoomType rt SET rt.deleted = true WHERE rt.property.id = :propertyId AND rt.deleted = false ")
    void softDeleteByPropertyId(@Param("propertyId") Integer propertyId);

    @Modifying
    @Query("UPDATE RoomType rt SET rt.deleted = false WHERE rt.property.id = :propertyId AND rt.deleted = false")
    void restoreByPropertyId(Integer propertyId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
