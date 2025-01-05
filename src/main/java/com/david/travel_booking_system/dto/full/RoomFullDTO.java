package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoomFullDTO {
    private Integer id;
    private Integer roomTypeId;
    private Integer floorNumber;
    private boolean active;
    private boolean available;
    private boolean cleaned;
    private boolean underMaintenance;

    private List<BookingBasicDTO> bookings;

    public RoomFullDTO(Integer id, Integer roomTypeId, Integer floorNumber, boolean active, boolean available,
                       boolean cleaned, boolean underMaintenance) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.floorNumber = floorNumber;
        this.active = active;
        this.available = available;
        this.cleaned = cleaned;
        this.underMaintenance = underMaintenance;
    }
}
