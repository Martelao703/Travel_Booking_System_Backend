package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.enums.BedType;
import com.david.travel_booking_system.model.Bed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BedBasicDTO {
    private Integer id;
    private BedType bedType;
    private Double length;
    private Double width;

    public static BedBasicDTO from(Bed bed) {
        return new BedBasicDTO(
                bed.getId(),
                bed.getBedType(),
                bed.getLength(),
                bed.getWidth()
        );
    }

    public static List<BedBasicDTO> from(List<Bed> beds) {
        return beds.stream().map(BedBasicDTO::from).collect(Collectors.toList());
    }
}
