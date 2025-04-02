package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.Room;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecifications extends BaseSpecifications {

    public static Specification<Room> filterByRoomTypeId(Integer roomTypeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("id"), roomTypeId);
    }

    public static Specification<Room> filterByPropertyId(Integer propertyId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("id"), propertyId);
    }
}
