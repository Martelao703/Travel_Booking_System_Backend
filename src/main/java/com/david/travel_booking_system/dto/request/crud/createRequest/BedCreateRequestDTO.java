package com.david.travel_booking_system.dto.request.crud.createRequest;

import com.david.travel_booking_system.enumsAndSets.BedType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BedCreateRequestDTO {
    @NotNull(message = "Room type ID cannot be null")
    private Integer roomTypeId;

    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @DecimalMin(value = "0.0", message = "Length cannot be less than 0")
    private Double length;

    @DecimalMin(value = "0.0", message = "Width cannot be less than 0")
    private Double width;
}
