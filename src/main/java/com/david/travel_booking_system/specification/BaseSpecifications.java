package com.david.travel_booking_system.specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.metamodel.SingularAttribute;

public class BaseSpecifications {

    public static <T> Specification<T> excludeDeleted(Class<T> entityClass) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
    }

    public static <T> Specification<T> filterById(Class<T> entityClass, Integer id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static <T, Y> Specification<T> hasAttribute(Class<T> entityClass, SingularAttribute<T, Y> attribute, Y value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(attribute), value);
    }
}
