package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.AtLeastOneFieldSet;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import lombok.Data;

@Data
@AtLeastOneFieldSet
public class RoomPatchRequestDTO {
    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Integer> floorNumber = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> cleaned = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> underMaintenance = OptionalFieldWrapper.unset();
}
