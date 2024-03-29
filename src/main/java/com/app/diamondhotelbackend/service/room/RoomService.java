package com.app.diamondhotelbackend.service.room;

import com.app.diamondhotelbackend.dto.room.model.RoomSelected;
import com.app.diamondhotelbackend.dto.room.request.AddRoomRequestDto;
import com.app.diamondhotelbackend.dto.room.response.RoomAvailableResponseDto;
import com.app.diamondhotelbackend.dto.room.response.RoomDetailsDto;
import com.app.diamondhotelbackend.dto.room.response.RoomSelectedCostResponseDto;
import com.app.diamondhotelbackend.entity.Room;
import com.app.diamondhotelbackend.exception.RoomProcessingException;
import com.app.diamondhotelbackend.exception.RoomTypeProcessingException;

import java.sql.Date;
import java.util.List;

public interface RoomService {

    Room createRoom(AddRoomRequestDto addRoomRequestDto);

    RoomAvailableResponseDto getRoomAvailableList(String checkIn, String checkOut, int rooms, int adults, int children, List<Long> roomTypeIdList, double pricePerHotelNight) throws RoomProcessingException;

    List<Room> getRoomAvailableList(Date checkIn, Date checkOut, RoomSelected roomSelected) throws RoomProcessingException;

    List<Integer> getRoomFloorList();

    RoomSelectedCostResponseDto getRoomSelectedCost(String checkIn, String checkOut, long roomTypeId, int rooms) throws RoomTypeProcessingException;

    Room getRoomByNumberAndFloor(int number, int floor);

    List<RoomDetailsDto> getRoomDetailsListByFloor(int floor, int page, int size);
}
