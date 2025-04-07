package com.david.travel_booking_system.dto.request.specialized;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.AtLeastOneFieldSet;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.annotation.WrappedFuture;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AtLeastOneFieldSet
public class BookingDateChangeRequestDTO {
    @NotNullIfExplicitlySet
    @WrappedFuture(message = "Planned Check-in date-time must be in the future")
    private OptionalFieldWrapper<LocalDateTime> plannedCheckInDateTime = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedFuture(message = "Planned Check-out date-time must be in the future")
    private OptionalFieldWrapper<LocalDateTime> plannedCheckOutDateTime = OptionalFieldWrapper.unset();
}
