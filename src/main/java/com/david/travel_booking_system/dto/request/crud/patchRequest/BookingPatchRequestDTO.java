package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@ValidDateRange
public class BookingPatchRequestDTO {
    @NotNullIfExplicitlySet
    @Min(value = 1, message = "Number of guests must be at least 1")
    private OptionalFieldWrapper<Integer> numberOfGuests = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Min(value = 0, message = "Total price must be greater than 0")
    private OptionalFieldWrapper<Double> totalPrice = OptionalFieldWrapper.unset();
}
