package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.enums.BedType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BedBasicDTO {
    private Integer id;
    private BedType bedType;
    private Double length;
    private Double width;
}
