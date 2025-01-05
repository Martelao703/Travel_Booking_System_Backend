package com.david.travel_booking_system.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomBasicDTO {
    private Integer id;
    private Integer roomTypeId;
    private Integer floorNumber;
    private boolean active;
    private boolean available;
    private boolean cleaned;
    private boolean underMaintenance;
}