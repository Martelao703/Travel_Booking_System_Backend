package com.david.travel_booking_system.dto.response.basic;

import com.david.travel_booking_system.enumsAndSets.BedType;
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
