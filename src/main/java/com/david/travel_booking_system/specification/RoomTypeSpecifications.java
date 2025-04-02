package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.RoomType;
import org.springframework.data.jpa.domain.Specification;

public class RoomTypeSpecifications extends BaseSpecifications {

    public static Specification<RoomType> filterByPropertyId(Integer propertyId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("property").get("id"), propertyId);
    }
}
