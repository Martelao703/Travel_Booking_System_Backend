package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.enums.BedType;
import com.david.travel_booking_system.model.Bed;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BedDTO {
    @NotNull(message = "Bed ID cannot be null")
    private Integer id;

    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @Min(value = 0, message = "Length cannot be less than 0")
    private Double length;

    @Min(value = 0, message = "Width cannot be less than 0")
    private Double width;

    public static BedDTO from(Bed bed) {
        return new BedDTO(
                bed.getId(),
                bed.getBedType(),
                bed.getLength(),
                bed.getWidth()
        );
    }

    public static List<BedDTO> from(List<Bed> beds) {
        return beds.stream().map(BedDTO::from).collect(Collectors.toList());
    }
}
