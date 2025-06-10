package com.david.travel_booking_system.repository;

import com.david.travel_booking_system.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>, JpaSpecificationExecutor<Token> {
    Optional<Token> findByToken(String token);
}
