package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.request.auth.AdminResetPasswordRequestDTO;
import com.david.travel_booking_system.dto.request.specialized.UserRoleChangeRequestDTO;
import com.david.travel_booking_system.dto.response.basic.*;
import com.david.travel_booking_system.dto.response.full.UserFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.UserPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
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
@RequestMapping("/api/users")
public class UserController {
    // Service
    private final UserService userService;
    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;
    private final BedService bedService;
    private final BookingService bookingService;

    // Mappers
    private final UserMapper userMapper;
    private final PropertyMapper propertyMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final BedMapper bedMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public UserController(UserService userService, PropertyService propertyService, RoomTypeService roomTypeService,
                          RoomService roomService, BedService bedService, BookingService bookingService,
                          UserMapper userMapper, PropertyMapper propertyMapper, RoomTypeMapper roomTypeMapper,
                          RoomMapper roomMapper, BedMapper bedMapper, BookingMapper bookingMapper) {
        this.userService = userService;
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
        this.bedService = bedService;
        this.bookingService = bookingService;
        this.userMapper = userMapper;
        this.propertyMapper = propertyMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
        this.bookingMapper = bookingMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    /* Create User endpoint delegated to AuthController endpoint 'register' */

    @GetMapping
    @PreAuthorize("@userPermissionChecker.canReadAny(authentication)")
    public ResponseEntity<List<UserBasicDTO>> getUsers() {
        List<UserBasicDTO> users = userMapper.toBasicDTOs(userService.getUsers(false));
        return ResponseEntity.ok(users); // Return 200 OK
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<UserFullDTO> getUser(@PathVariable Integer id) {
        UserFullDTO user = userMapper.toFullDTO(userService.getUserByIdWithCollections(id, false));
        return ResponseEntity.ok(user); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    @PreAuthorize("@userPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<UserBasicDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserUpdateRequestDTO updateDTO
    ) {
        UserBasicDTO updatedUser = userMapper.toBasicDTO(userService.updateUser(id, updateDTO));
        return ResponseEntity.ok(updatedUser); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    @PreAuthorize("@userPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<UserBasicDTO> patchUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserPatchRequestDTO patchDTO
    ) {
        UserBasicDTO patchedUser = userMapper.toBasicDTO(userService.patchUser(id, patchDTO));
        return ResponseEntity.ok(patchedUser); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@userPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("@userPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable Integer id) {
        userService.hardDeleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("@userPermissionChecker.canRestore(authentication, #id)")
    public ResponseEntity<Void> restoreUser(@PathVariable Integer id) {
        userService.restoreUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/properties")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<List<PropertyBasicDTO>> getProperties(@PathVariable Integer id) {
        List<PropertyBasicDTO> properties = propertyMapper.toBasicDTOs(
                propertyService.getPropertiesByOwnerId(id, false)
        );
        return ResponseEntity.ok(properties); // Return 200 OK
    }

    @GetMapping("/{id}/room-types")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes(@PathVariable Integer id) {
        List<RoomTypeBasicDTO> roomTypes = roomTypeMapper.toBasicDTOs(
                roomTypeService.getRoomTypesByOwnerId(id, false)
        );
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}/rooms")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<List<RoomBasicDTO>> getRooms(@PathVariable Integer id) {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(
                roomService.getRoomsByOwnerId(id, false)
        );
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}/beds")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<List<BedBasicDTO>> getBeds(@PathVariable Integer id) {
        List<BedBasicDTO> beds = bedMapper.toBasicDTOs(
                bedService.getBedsByOwnerId(id, false)
        );
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    @GetMapping("/{id}/bookings")
    @PreAuthorize("@userPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<List<BookingBasicDTO>> getBookings(@PathVariable Integer id) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByUserId(id, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Custom endpoints -------------------------------------------------------------------------------------------- */

    @PatchMapping("/{id}/activate")
    @PreAuthorize("@userPermissionChecker.canActivate(authentication, #id)")
    public ResponseEntity<Void> activateUser(@PathVariable Integer id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@userPermissionChecker.canDeactivate(authentication, #id)")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/roles")
    @PreAuthorize("@userPermissionChecker.canChangeRoles(authentication, #id, #dto.roles)")
    public ResponseEntity<Void> changeUserRoles(
            @PathVariable Integer id,
            @Valid @RequestBody UserRoleChangeRequestDTO dto
    ) {
        userService.changeUserRoles(id, dto.getRoles());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("@userPermissionChecker.canResetUserPassword(authentication)")
    public ResponseEntity<Void> adminResetPassword(
            @PathVariable Integer id,
            @RequestBody @Valid AdminResetPasswordRequestDTO dto
    ) {
        userService.adminResetPassword(id, dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
