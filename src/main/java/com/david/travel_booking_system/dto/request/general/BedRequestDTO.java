package com.david.travel_booking_system.dto.request.general;

import com.david.travel_booking_system.enumsAndSets.BedType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BedRequestDTO {
    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @Min(value = 0, message = "Length cannot be less than 0")
    private Double length;

    @Min(value = 0, message = "Width cannot be less than 0")
    private Double width;
}
