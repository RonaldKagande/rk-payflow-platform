package com.rkpayflow.account.interfaces.rest.dto;

import com.rkpayflow.account.domain.Account;
import com.rkpayflow.account.domain.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String accountNumber,
        String customerName,
        String email,
        BigDecimal balance,
        Currency currency,
        AccountStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getCustomerName(),
                account.getEmail(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
