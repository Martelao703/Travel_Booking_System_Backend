package com.david.travel_booking_system.dto.createRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomCreateRequestDTO {
    @NotNull(message = "Room type ID cannot be null")
    private Integer roomTypeId;

    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean isCleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean isUnderMaintenance;
}
