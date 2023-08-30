package com.app.diamondhotelbackend.service.roomtypeopinion;

import com.app.diamondhotelbackend.dto.roomtype.RoomTypeOpinionSummaryDto;
import com.app.diamondhotelbackend.repository.RoomTypeOpinionRepository;
import com.app.diamondhotelbackend.util.ConstantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RoomTypeOpinionServiceImpl implements RoomTypeOpinionService {

    private final RoomTypeOpinionRepository roomTypeOpinionRepository;

    @Override
    public RoomTypeOpinionSummaryDto getRoomTypeOpinionSummaryDto(String roomTypeName) {
        long amount = roomTypeOpinionRepository.countAllByRoomTypeName(roomTypeName);
        double rate = roomTypeOpinionRepository.findAverageRateByRoomTypeName(roomTypeName);
        String text = getTextRepresentedRate(rate);

        return RoomTypeOpinionSummaryDto.builder().amount(amount).rate(rate).text(text).build();
    }

    private String getTextRepresentedRate(double rate) {
        if (rate < 5) {
            return ConstantUtil.BAD;
        } else if (rate < 8) {
            return ConstantUtil.GOOD;
        } else {
            return ConstantUtil.EXCELLENT;
        }
    }
}
