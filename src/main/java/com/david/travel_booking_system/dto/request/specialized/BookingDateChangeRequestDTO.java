package com.david.travel_booking_system.dto.request.specialized;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidDateRange
public class BookingDateChangeRequestDTO {
    @NotNullIfExplicitlySet
    @Future(message = "Planned Check-in date-time must be in the future")
    private OptionalFieldWrapper<LocalDateTime> plannedCheckInDateTime = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Future(message = "Planned Check-out date-time must be in the future")
    private OptionalFieldWrapper<LocalDateTime> plannedCheckOutDateTime = OptionalFieldWrapper.unset();
}
