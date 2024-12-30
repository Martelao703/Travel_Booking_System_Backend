package com.david.travel_booking_system.dto.request.updateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomUpdateRequestDTO {
    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;

    @NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean isCleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;
}
