package com.app.diamondhotelbackend.dto.roomtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeDto {

    private long id;

    private String name;

    private int capacity;

    @JsonProperty("price_per_hotel_night")
    private BigDecimal pricePerHotelNight;

    @JsonProperty("equipment_list")
    private List<String> equipmentList;

    private String image;

    private RoomTypeOpinionSummaryDto opinion;
}