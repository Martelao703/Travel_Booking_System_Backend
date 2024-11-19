package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.RoomDTO;
import com.david.travel_booking_system.dto.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public RoomDTO createRoom(@RequestBody RoomCreateRequestDTO roomCreateRequestDTO) {
        return RoomDTO.from(roomService.createRoom(roomCreateRequestDTO));
    }

    @GetMapping
    public List<RoomDTO> getRooms() {
        return RoomDTO.from(roomService.getRooms());
    }

    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable Integer id) {
        return RoomDTO.from(roomService.getRoomById(id));
    }
}
