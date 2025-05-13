package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
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

    public static Specification<Room> filterByOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("owner").get("id"), ownerId);
    }

    public static Specification<Room> filterByOwnerEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roomType").get("property").get("owner").get("email"), email);
    }
}
