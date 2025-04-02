package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /* CRUD and Basic methods -------------------------------------------------------------------------------------- */

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query(" UPDATE User u SET u.deleted = true WHERE u.id = :id ")
    void softDeleteById(Integer id);

    /* Custom methods ---------------------------------------------------------------------------------------------- */
}
