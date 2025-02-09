package com.david.travel_booking_system.dto.request.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.NotNullIfExplicitlySet;
import lombok.Data;

@Data
public class RoomPatchRequestDTO {
    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Integer> floorNumber = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> active = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> available = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> cleaned = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> underMaintenance = OptionalFieldWrapper.unset();
}
