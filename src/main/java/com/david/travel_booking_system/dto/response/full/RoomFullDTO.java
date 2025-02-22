package com.david.travel_booking_system.dto.response.full;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoomFullDTO {
    private Integer id;
    private Integer roomTypeId;
    private Integer floorNumber;
    private boolean active;
    private boolean occupied;
    private boolean cleaned;
    private boolean underMaintenance;

    private List<BookingBasicDTO> bookings;

    public RoomFullDTO(Integer id, Integer roomTypeId, Integer floorNumber, boolean active, boolean occupied,
                       boolean cleaned, boolean underMaintenance) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.floorNumber = floorNumber;
        this.active = active;
        this.occupied = occupied;
        this.cleaned = cleaned;
        this.underMaintenance = underMaintenance;
    }
}
