package com.app.diamondhotelbackend.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentChargeRequestDto {

    @JsonProperty("payment_id")
    private long paymentId;

    @JsonProperty("reservation_id")
    private long reservationId;

    @JsonProperty("user_profile_id")
    private long userProfileId;

    private String token;

    private int amount;
}