package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.Bed;
import org.springframework.data.jpa.domain.Specification;

public class BedSpecifications extends BaseSpecifications {

    public static Specification<Bed> filterByRoomTypeId(Integer roomTypeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("id"), roomTypeId);
    }

    public static Specification<Bed> filterByPropertyId(Integer propertyId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("id"), propertyId);
    }

    public static Specification<Bed> filterByOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("owner").get("id"), ownerId);
    }

    public static Specification<Bed> filterByOwnerEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("owner").get("email"), email);
    }
}
