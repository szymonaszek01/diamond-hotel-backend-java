package com.app.diamondhotelbackend.controller;

import com.app.diamondhotelbackend.dto.room.request.AddRoomRequestDto;
import com.app.diamondhotelbackend.dto.room.response.RoomAvailableResponseDto;
import com.app.diamondhotelbackend.dto.room.response.RoomDetailsDto;
import com.app.diamondhotelbackend.dto.room.response.RoomSelectedCostResponseDto;
import com.app.diamondhotelbackend.entity.Room;
import com.app.diamondhotelbackend.exception.RoomProcessingException;
import com.app.diamondhotelbackend.service.room.RoomServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Tag(name = "Room", description = "Room management APIs")
@RequestMapping("/api/v1/room")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://diamond-hotel-frontend.vercel.app/"}, allowCredentials = "true")
public class RoomController {

    private final RoomServiceImpl roomService;

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody AddRoomRequestDto addRoomRequestDto) {
        try {
            return ResponseEntity.ok(roomService.createRoom(addRoomRequestDto));
        } catch (RoomProcessingException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/all/available")
    public ResponseEntity<RoomAvailableResponseDto> getRoomAvailabilityList(@RequestParam(value = "check-in") String checkIn,
                                                                            @RequestParam(value = "check-out") String checkOut,
                                                                            @RequestParam(value = "rooms") int rooms,
                                                                            @RequestParam(value = "adults") int adults,
                                                                            @RequestParam(value = "children") int children,
                                                                            @RequestParam(value = "room-type-id", required = false) List<Long> roomTypeIdList,
                                                                            @RequestParam(value = "price-per-hotel-night", defaultValue = "0", required = false) double pricePerHotelNight) {
        try {
            return ResponseEntity.ok(roomService.getRoomAvailableList(checkIn, checkOut, rooms, adults, children, roomTypeIdList, pricePerHotelNight));
        } catch (RoomProcessingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/cost")
    public ResponseEntity<RoomSelectedCostResponseDto> getRoomSelectedCost(@RequestParam(value = "check-in") String checkIn,
                                                                           @RequestParam(value = "check-out") String checkOut,
                                                                           @RequestParam(value = "rooms") int rooms,
                                                                           @RequestParam(value = "room-type-id") long roomTypeId) {
        try {
            return ResponseEntity.ok(roomService.getRoomSelectedCost(checkIn, checkOut, roomTypeId, rooms));
        } catch (RoomProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/all/floors")
    public ResponseEntity<List<Integer>> getRoomFloorList() {
        try {
            return ResponseEntity.ok(roomService.getRoomFloorList());
        } catch (RoomProcessingException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/all/floor/{floor}/details")
    public ResponseEntity<List<RoomDetailsDto>> getRoomDetailsListByFloor(@PathVariable int floor,
                                                                          @RequestParam(value = "page") int page,
                                                                          @RequestParam(value = "size") int size
    ) {
        try {
            return ResponseEntity.ok(roomService.getRoomDetailsListByFloor(floor, page, size));
        } catch (RoomProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
