package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.Property;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecifications extends BaseSpecifications {

    public static Specification<Property> filterByOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Property> filterByOwnerEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("owner").get("email"), email);
    }
}
