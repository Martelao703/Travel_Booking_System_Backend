package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Property;
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

    /* By Owner (User) */

    @Modifying
    @Query("""
            UPDATE Property p SET p.deleted = true, p.active = false
            WHERE p.owner.id = :ownerId AND p.deleted = false
            """)
    void softDeleteByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Property p SET p.deleted = false
            WHERE p.owner.id = :ownerId AND p.deleted = true
            """)
    void restoreByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Property p SET p.active = true
            WHERE p.owner.id = :ownerId AND p.deleted = false
            """)
    void activateByOwnerId(@Param("ownerId") Integer ownerId);

    @Modifying
    @Query("""
            UPDATE Property p SET p.active = false
            WHERE p.owner.id = :ownerId AND p.deleted = false
            """)
    void deactivateByOwnerId(@Param("ownerId") Integer ownerId);

    /* Custom methods ---------------------------------------------------------------------------------------------- */

}
