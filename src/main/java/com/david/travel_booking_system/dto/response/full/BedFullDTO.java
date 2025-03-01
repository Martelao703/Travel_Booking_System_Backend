package com.david.travel_booking_system.dto.response.full;

import com.david.travel_booking_system.enumsAndSets.BedType;
import lombok.Data;

import java.util.List;

@Data
public class BedFullDTO {
    private Integer id;
    private BedType bedType;
    private Double length;
    private Double width;

    private List<Integer> roomTypeIds;

    public BedFullDTO(Integer id, BedType bedType, Double length, Double width) {
        this.id = id;
        this.bedType = bedType;
        this.length = length;
        this.width = width;
    }
}
