package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    @Modifying
    @Query(" UPDATE Property p SET p.deleted = true WHERE p.owner.id = :ownerId AND p.deleted = false ")
    void softDeleteByOwnerId(Integer ownerId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */

}
