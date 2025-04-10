package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.*;
import com.david.travel_booking_system.dto.response.detail.PropertyDetailDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.PropertyCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.PropertyFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.PropertyPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.PropertyUpdateRequestDTO;
import com.david.travel_booking_system.mapper.*;
import com.david.travel_booking_system.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    // Services
    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;
    private final BedService bedService;
    private final BookingService bookingService;

    // Mappers
    private final PropertyMapper propertyMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final BedMapper bedMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public PropertyController(PropertyService propertyService, RoomTypeService roomTypeService, RoomService roomService,
                              BedService bedService, BookingService bookingService, PropertyMapper propertyMapper,
                              RoomTypeMapper roomTypeMapper, RoomMapper roomMapper, BedMapper bedMapper,
                              BookingMapper bookingMapper) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
        this.bedService = bedService;
        this.bookingService = bookingService;
        this.propertyMapper = propertyMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
        this.bookingMapper = bookingMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    @PreAuthorize("hasAnyRole('ROLE_HOST', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PropertyDetailDTO> createProperty(
            @RequestBody @Valid PropertyCreateRequestDTO propertyCreateRequestDTO
    ) {
        PropertyDetailDTO createdProperty = propertyMapper.toDetailDTO(
                propertyService.createProperty(propertyCreateRequestDTO)
        );
        return ResponseEntity.status(201).body(createdProperty); // Return 201 Created
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_HOST')")
    @GetMapping
    public ResponseEntity<List<PropertyBasicDTO>> getProperties() {
        List<PropertyBasicDTO> properties = propertyMapper.toBasicDTOs(propertyService.getProperties(false));
        return ResponseEntity.ok(properties); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyFullDTO> getProperty(@PathVariable Integer id) {
        PropertyFullDTO property = propertyMapper.toFullDTO(propertyService.getPropertyById(id, false));
        return ResponseEntity.ok(property); // Return 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDetailDTO> updateProperty(
            @PathVariable Integer id,
            @RequestBody @Valid PropertyUpdateRequestDTO propertyUpdateRequestDTO
    ) {
        PropertyDetailDTO updatedPropertyDTO = propertyMapper.toDetailDTO(
                propertyService.updateProperty(id, propertyUpdateRequestDTO)
        );
        return ResponseEntity.ok(updatedPropertyDTO); // Return 200 OK
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PropertyDetailDTO> patchProperty(
            @PathVariable Integer id,
            @RequestBody @Valid PropertyPatchRequestDTO propertyPatchRequestDTO
    ) {
        PropertyDetailDTO patchedPropertyDTO = propertyMapper.toDetailDTO(
                propertyService.patchProperty(id, propertyPatchRequestDTO)
        );
        return ResponseEntity.ok(patchedPropertyDTO); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteProperty(@PathVariable Integer id) {
        propertyService.softDeleteProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProperty(@PathVariable Integer id) {
        propertyService.hardDeleteProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProperty(@PathVariable Integer id) {
        propertyService.restoreProperty(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/roomTypes")
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes(@PathVariable Integer id) {
        List<RoomTypeBasicDTO> roomTypes = roomTypeMapper.toBasicDTOs(
                roomTypeService.getRoomTypesByPropertyId(id, false)
        );
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomBasicDTO>> getRooms(@PathVariable Integer id) {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(roomService.getRoomsByPropertyId(id, false));
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}/beds")
    public ResponseEntity<List<BedBasicDTO>> getBeds(@PathVariable Integer id) {
        List<BedBasicDTO> beds = bedMapper.toBasicDTOs(bedService.getBedsByPropertyId(id, false));
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingBasicDTO>> getBookings(@PathVariable Integer id) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByPropertyId(id, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateProperty(@PathVariable Integer id) {
        propertyService.activateProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProperty(@PathVariable Integer id) {
        propertyService.deactivateProperty(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
