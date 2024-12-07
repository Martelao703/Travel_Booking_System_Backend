package com.david.travel_booking_system.dto.detail;

import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.enums.BedType;
import com.david.travel_booking_system.model.Bed;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BedDetailDTO {
    @NotNull(message = "Bed ID cannot be null")
    private Integer id;

    @NotNull(message = "Bed type cannot be null")
    private BedType bedType;

    @Min(value = 0, message = "Length cannot be less than 0")
    private Double length;

    @Min(value = 0, message = "Width cannot be less than 0")
    private Double width;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<RoomTypeDTO> roomTypes;

    public BedDetailDTO(Integer id, BedType bedType, Double length, Double width) {
        this.id = id;
        this.bedType = bedType;
        this.length = length;
        this.width = width;
    }

    public static BedDetailDTO from(Bed bed) {
        BedDetailDTO bedDetailDTO = new BedDetailDTO(
                bed.getId(),
                bed.getBedType(),
                bed.getLength(),
                bed.getWidth()
        );
        bedDetailDTO.setRoomTypes(RoomTypeDTO.from(bed.getRoomTypes()));

        return bedDetailDTO;
    }

    public static List<BedDetailDTO> from(List<Bed> beds) {
        return beds.stream().map(BedDetailDTO::from).collect(Collectors.toList());
    }
}
