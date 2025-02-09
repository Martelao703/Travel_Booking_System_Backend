package com.david.travel_booking_system.dto.request.updateRequest;

import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ValidDateRange
public class BookingUpdateRequestDTO {
    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be greater than 0")
    private double totalPrice;
}
