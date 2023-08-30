package com.app.diamondhotelbackend.service.transaction;


import com.app.diamondhotelbackend.dto.shoppingcart.CostSummaryDto;
import com.app.diamondhotelbackend.dto.transaction.TransactionDto;
import com.app.diamondhotelbackend.dto.transaction.TransactionStatusInfoDto;
import com.app.diamondhotelbackend.entity.Reservation;
import com.app.diamondhotelbackend.entity.Transaction;
import com.app.diamondhotelbackend.exception.InvalidTransactionStatusException;
import com.app.diamondhotelbackend.exception.TransactionNotFoundException;
import com.app.diamondhotelbackend.repository.TransactionRepository;
import com.app.diamondhotelbackend.util.ConstantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction findTransactionByCode(String code) throws TransactionNotFoundException {
        return transactionRepository.findTransactionByCode(code).orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

    @Override
    public void updateTransactionAfterReservationCancellation(Reservation reservation) {
        Transaction transaction = reservation.getTransaction();
        long totalWithoutTax = transaction.getTotalWithoutTax().longValue() - reservation.getRoomCost().longValue();
        transaction.setTotalWithoutTax(new BigDecimal(totalWithoutTax));
        transaction.setTax(new BigDecimal(totalWithoutTax * 0.1));

        transactionRepository.save(transaction);
    }

    @Override
    public TransactionDto toTransactionDtoMapper(Transaction transaction) {
        return TransactionDto.builder()
                .code(transaction.getCode())
                .totalWithoutTax(transaction.getTotalWithoutTax())
                .tax(transaction.getTax())
                .carRent(transaction.getCarRent())
                .carPickUp(transaction.getCarPickUp())
                .status(transaction.getStatus())
                .build();
    }

    @Override
    public TransactionStatusInfoDto changeTransactionStatus(TransactionStatusInfoDto transactionStatusInfoDto) throws TransactionNotFoundException, InvalidTransactionStatusException {
        Transaction transaction = findTransactionByCode(transactionStatusInfoDto.getCode());
        if (!isValidTransactionStatus(transactionStatusInfoDto.getStatus())) {
            throw new InvalidTransactionStatusException("Invalid transaction's status");
        }

        transaction.setStatus(transactionStatusInfoDto.getStatus());
        transactionRepository.save(transaction);

        return transactionStatusInfoDto;
    }

    @Override
    public Transaction createNewTransaction(CostSummaryDto costSummaryDto) {
        return transactionRepository.save(Transaction.builder()
                .code(generateTransactionCode())
                .totalWithoutTax(costSummaryDto.getTotalWithoutTax())
                .carPickUp(costSummaryDto.getCarPickUp())
                .carRent(costSummaryDto.getCarRent())
                .tax(costSummaryDto.getTax())
                .status(ConstantUtil.WAITING_FOR_PAYMENT)
                .build());
    }

    @Override
    public BigDecimal getTotalCost(Transaction transaction) {
        return new BigDecimal(transaction.getTotalWithoutTax().longValue() + transaction.getTax().longValue() + transaction.getCarPickUp().longValue() + transaction.getCarRent().longValue());
    }

    private String generateTransactionCode() {
        Random random = new Random();
        String transactionCode = "#";
        do {
            transactionCode += random.nextInt(90000000) + 10000000;
        } while (transactionRepository.findTransactionByCode(transactionCode).isPresent());

        return transactionCode;
    }

    private boolean isValidTransactionStatus(String status) {
        return ConstantUtil.APPROVED.equals(status) || ConstantUtil.WAITING_FOR_PAYMENT.equals(status) || ConstantUtil.CANCELLED.equals(status);
    }
}
