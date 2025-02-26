package com.david.travel_booking_system.dto.request.crud.updateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomUpdateRequestDTO {
    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Active status cannot be null")
    private boolean active;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean cleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;
}
