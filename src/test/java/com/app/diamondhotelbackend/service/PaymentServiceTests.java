package com.app.diamondhotelbackend.service;

import com.app.diamondhotelbackend.dto.payment.request.PaymentCancelRequestDto;
import com.app.diamondhotelbackend.dto.payment.request.PaymentChargeRequestDto;
import com.app.diamondhotelbackend.entity.Payment;
import com.app.diamondhotelbackend.entity.UserProfile;
import com.app.diamondhotelbackend.repository.PaymentRepository;
import com.app.diamondhotelbackend.service.email.EmailServiceImpl;
import com.app.diamondhotelbackend.service.payment.PaymentServiceImpl;
import com.app.diamondhotelbackend.service.stripe.StripeServiceImpl;
import com.app.diamondhotelbackend.service.userprofile.UserProfileServiceImpl;
import com.app.diamondhotelbackend.util.ConstantUtil;
import com.app.diamondhotelbackend.util.PdfUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private StripeServiceImpl stripeService;

    @Mock
    private UserProfileServiceImpl userProfileService;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private PdfUtil pdfUtil;

    private PaymentChargeRequestDto paymentChargeRequestDto;

    private PaymentCancelRequestDto paymentCancelRequestDto;

    private Charge charge;

    private Refund refund;

    private UserProfile userProfile;

    private InputStreamResource inputStreamResource;

    private Payment payment;

    private Payment updatedPayment;

    @BeforeEach
    public void init() {
        paymentChargeRequestDto = PaymentChargeRequestDto.builder()
                .paymentId(1)
                .reservationId(1)
                .userProfileId(1)
                .amount(200)
                .token("token1")
                .build();

        paymentCancelRequestDto = PaymentCancelRequestDto.builder()
                .paymentId(1)
                .reservationId(1)
                .userProfileId(1)
                .build();

        charge = new Charge();
        charge.setId("chargeId1");
        charge.setStatus(ConstantUtil.SUCCEEDED);

        refund = new Refund();
        refund.setId("chargeId1");
        refund.setObject(ConstantUtil.REFUND);

        userProfile = UserProfile.builder()
                .id(1)
                .email("email1")
                .build();

        inputStreamResource = new InputStreamResource(InputStream.nullInputStream());

        payment = Payment.builder()
                .id(1)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .status(ConstantUtil.WAITING_FOR_PAYMENT)
                .build();

        updatedPayment = Payment.builder()
                .id(1)
                .createdAt(new Date(System.currentTimeMillis()))
                .expiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .token("token1")
                .cost(BigDecimal.valueOf(1650))
                .status(ConstantUtil.APPROVED)
                .build();
    }

    @Test
    void PaymentService_GetPaymentById_ReturnsPayment() {
        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));

        Payment foundPayment = paymentService.getPaymentById(payment.getId());

        Assertions.assertThat(foundPayment).isNotNull();
        Assertions.assertThat(foundPayment.getId()).isEqualTo(payment.getId());
    }

    @Test
    public void PaymentService_CreatePayment_ReturnsPayment() {
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);

        Payment savedPayment = paymentService.createPayment(payment);

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getToken()).isEqualTo(payment.getToken());
    }

    @Test
    public void PaymentService_ChargePayment_ReturnsPayment() throws StripeException {
        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        when(stripeService.createCharge(Mockito.any(String.class), Mockito.any(int.class))).thenReturn(charge);
        when(userProfileService.getUserProfileById(Mockito.any(long.class))).thenReturn(userProfile);
        when(pdfUtil.getPaymentPdf(Mockito.any(Payment.class), Mockito.any(UserProfile.class), Mockito.any(long.class))).thenReturn(inputStreamResource);

        Payment savedPayment = paymentService.chargePayment(paymentChargeRequestDto);

        verify(emailService).sendPaymentForReservationConfirmedEmail(Mockito.any(Payment.class), Mockito.any(UserProfile.class), Mockito.any(long.class), Mockito.any(InputStreamResource.class));

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getToken()).isEqualTo("token1");
        Assertions.assertThat(savedPayment.getCharge()).isEqualTo(charge.getId());
        Assertions.assertThat(savedPayment.getStatus()).isEqualTo(ConstantUtil.APPROVED);
    }

    @Test
    public void PaymentService_CancelPayment_ReturnsPayment() throws StripeException {
        payment.setCharge("chargeId1");

        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        when(stripeService.createRefund(Mockito.any(String.class))).thenReturn(refund);
        when(userProfileService.getUserProfileById(Mockito.any(long.class))).thenReturn(userProfile);
        when(pdfUtil.getPaymentPdf(Mockito.any(Payment.class), Mockito.any(UserProfile.class), Mockito.any(long.class))).thenReturn(inputStreamResource);

        Payment savedPayment = paymentService.cancelPayment(paymentCancelRequestDto);

        verify(emailService).sendPaymentForReservationCancelledEmail(Mockito.any(Payment.class), Mockito.any(UserProfile.class), Mockito.any(long.class), Mockito.any(InputStreamResource.class));

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getStatus()).isEqualTo(ConstantUtil.CANCELLED);
    }

    @Test
    public void PaymentService_UpdatePaymentStatus_ReturnsPayment() {
        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(updatedPayment);

        Payment savedPayment = paymentService.updatePaymentStatus(1, ConstantUtil.APPROVED);

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getStatus()).isEqualTo(updatedPayment.getStatus());
    }

    @Test
    public void PaymentService_UpdatePaymentCost_ReturnsPayment() {
        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(updatedPayment);

        Payment savedPayment = paymentService.updatePaymentCost(1, BigDecimal.valueOf(1650));

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getCost()).isEqualTo(updatedPayment.getCost());
    }

    @Test
    public void PaymentService_UpdatePaymentToken_ReturnsPayment() {
        when(paymentRepository.findById(Mockito.any(long.class))).thenReturn(Optional.of(payment));
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(updatedPayment);

        Payment savedPayment = paymentService.updatePaymentToken(1, "token1");

        Assertions.assertThat(savedPayment).isNotNull();
        Assertions.assertThat(savedPayment.getToken()).isEqualTo(updatedPayment.getToken());
    }
}