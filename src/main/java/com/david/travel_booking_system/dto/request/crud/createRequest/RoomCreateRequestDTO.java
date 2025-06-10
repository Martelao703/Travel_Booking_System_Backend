package com.david.travel_booking_system.dto.request.crud.createRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomCreateRequestDTO {
    @NotNull(message = "Room type ID cannot be null")
    private Integer roomTypeId;

    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    @NotNull(message = "Cleanliness status cannot be null")
    private boolean cleaned;

    @NotNull(message = "Maintenance status cannot be null")
    private boolean underMaintenance;
}
