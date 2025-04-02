package com.david.travel_booking_system.specification;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.model.Booking;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookingSpecifications extends BaseSpecifications {

    public static Specification<Booking> filterByUserId(Integer userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Booking> filterByRoomId(Integer roomId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("room").get("id"), roomId);
    }

    public static Specification<Booking> filterByRoomTypeId(Integer roomTypeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("room").get("roomType").get("id"), roomTypeId);
    }

    public static Specification<Booking> filterByPropertyId(Integer propertyId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("room").get("roomType").get("property").get("id"), propertyId);
    }

    public static Specification<Booking> filterByStatus(BookingStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Booking> filterByStatuses(List<BookingStatus> statuses) {
        return (root, query, criteriaBuilder) ->
                root.get("status").in(statuses);
    }
}
