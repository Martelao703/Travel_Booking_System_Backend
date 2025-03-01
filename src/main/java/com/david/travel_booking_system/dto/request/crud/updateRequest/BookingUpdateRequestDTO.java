package com.david.travel_booking_system.dto.request.crud.updateRequest;

import com.david.travel_booking_system.validation.annotation.ValidDateRange;
import jakarta.validation.constraints.DecimalMin;
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
    @DecimalMin(value = "0.0", message = "Total price must be greater than 0")
    private double totalPrice;
}
