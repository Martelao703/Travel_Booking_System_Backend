package com.david.travel_booking_system.service;


import com.david.travel_booking_system.model.Token;
import com.david.travel_booking_system.repository.TokenRepository;
import com.david.travel_booking_system.security.TokenType;
import com.david.travel_booking_system.specification.TokenSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TokenService {

    private final TokenRepository tokens;

    @Autowired
    public TokenService(TokenRepository tokens) {
        this.tokens = tokens;
    }

    @Transactional
    public void saveToken(Token token) {
        tokens.save(token);
    }

    @Transactional
    public void revokeUserTokensByType(Integer userId, TokenType type) {
        // Filter by valid tokens for the user and the specified type
        Specification<Token> spec = TokenSpecifications.filterValidByUser(userId)
                .and(TokenSpecifications.filterByType(type));
        List<Token> valid = tokens.findAll(spec);

        // Revoke valid tokens
        valid.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokens.saveAll(valid);
    }

    @Transactional
    public void revokeTokenByType(String rawToken, TokenType type) {
        // Filter by token and type
        Specification<Token> spec = TokenSpecifications.filterByToken(rawToken)
                .and(TokenSpecifications.filterByType(type));

        // Revoke the token if it exists
        tokens.findOne(spec).ifPresent(t -> {
            t.setExpired(true);
            t.setRevoked(true);
            tokens.save(t);
        });
    }

    @Transactional
    public void revokeAllUserTokens(Integer userId) {
        // Filter by valid tokens for the user
        Specification<Token> spec = TokenSpecifications.filterValidByUser(userId);
        List<Token> valid = tokens.findAll(spec);

        // Revoke all valid tokens
        valid.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokens.saveAll(valid);
    }
}
