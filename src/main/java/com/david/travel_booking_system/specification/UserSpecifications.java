package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications extends BaseSpecifications {
    public static Specification<User> filterByEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<User> filterByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
    }
}
