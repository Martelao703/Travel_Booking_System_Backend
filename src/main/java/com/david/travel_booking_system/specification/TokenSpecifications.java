package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.Token;
import com.david.travel_booking_system.security.TokenType;
import org.springframework.data.jpa.domain.Specification;

public class TokenSpecifications extends BaseSpecifications {

    public static Specification<Token> filterByUserId(Integer userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Token> filterByToken(String token) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("token"), token);
    }

    public static Specification<Token> filterByType(TokenType type) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tokenType"), type);
    }

    public static Specification<Token> excludeExpired() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("expired"), false);
    }

    public static Specification<Token> excludeRevoked() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("revoked"), false);
    }

    /* Convenience only */

    public static Specification<Token> filterValid() {
        return excludeExpired().and(excludeRevoked());
    }

    public static Specification<Token> filterValidByUser(Integer userId) {
        return filterByUserId(userId).and(filterValid());
    }
}
