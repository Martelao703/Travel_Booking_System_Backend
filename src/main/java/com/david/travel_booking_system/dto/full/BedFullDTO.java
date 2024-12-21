package com.david.travel_booking_system.dto.full;

import com.david.travel_booking_system.dto.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.enums.BedType;
import com.david.travel_booking_system.model.Bed;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BedFullDTO {
    private Integer id;
    private BedType bedType;
    private Double length;
    private Double width;

    private List<RoomTypeBasicDTO> roomTypes;

    public BedFullDTO(Integer id, BedType bedType, Double length, Double width) {
        this.id = id;
        this.bedType = bedType;
        this.length = length;
        this.width = width;
    }

    public static BedFullDTO from(Bed bed) {
        BedFullDTO bedFullDTO = new BedFullDTO(
                bed.getId(),
                bed.getBedType(),
                bed.getLength(),
                bed.getWidth()
        );
        bedFullDTO.setRoomTypes(RoomTypeBasicDTO.from(bed.getRoomTypes()));

        return bedFullDTO;
    }

    public static List<BedFullDTO> from(List<Bed> beds) {
        return beds.stream().map(BedFullDTO::from).collect(Collectors.toList());
    }
}
