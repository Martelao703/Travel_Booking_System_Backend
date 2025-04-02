package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.enumsAndSets.BedType;
import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.annotation.WrappedMax;
import com.david.travel_booking_system.validation.annotation.WrappedMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BedPatchRequestDTO {
    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<BedType> bedType = OptionalFieldWrapper.unset();

    @WrappedMin(value = 0, message = "Length cannot be less than 0")
    private OptionalFieldWrapper<Double> length = OptionalFieldWrapper.unset();

    @WrappedMin(value = 0, message = "Width cannot be less than 0")
    private OptionalFieldWrapper<Double> width = OptionalFieldWrapper.unset();
}
