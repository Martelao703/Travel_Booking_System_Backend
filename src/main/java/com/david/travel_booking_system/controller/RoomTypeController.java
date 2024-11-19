package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.dto.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    public RoomTypeDetailDTO createRoomType(@RequestBody RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        return RoomTypeDetailDTO.from(roomTypeService.createRoomType(roomTypeCreateRequestDTO));
    }

    @GetMapping
    public List<RoomTypeDTO> getRoomTypes() {
        return RoomTypeDTO.from(roomTypeService.getRoomTypes());
    }

    @GetMapping("/{id}")
    public RoomTypeDetailDTO getRoomType(@PathVariable Integer id) {
        return RoomTypeDetailDTO.from(roomTypeService.getRoomTypeById(id));
    }
}
