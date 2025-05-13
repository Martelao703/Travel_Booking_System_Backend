package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.RoomType;
import org.springframework.data.jpa.domain.Specification;

public class RoomTypeSpecifications extends BaseSpecifications {

    public static Specification<RoomType> filterByPropertyId(Integer propertyId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("property").get("id"), propertyId);
    }

    public static Specification<RoomType> filterByOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("property").get("owner").get("id"), ownerId);
    }

    public static Specification<RoomType> filterByOwnerEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("property").get("owner").get("email"), email);
    }
}
