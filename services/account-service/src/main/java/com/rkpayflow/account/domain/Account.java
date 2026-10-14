package com.rkpayflow.account.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final String accountNumber;
    private final String customerName;
    private final String email;
    private BigDecimal balance;
    private final Currency currency;
    private AccountStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Account(
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
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.email = email;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account open(
            String accountNumber,
            String customerName,
            String email,
            BigDecimal openingBalance,
            Currency currency
    ) {
        Objects.requireNonNull(accountNumber, "Account number is required");
        Objects.requireNonNull(customerName, "Customer name is required");
        Objects.requireNonNull(email, "Email is required");
        Objects.requireNonNull(openingBalance, "Opening balance is required");
        Objects.requireNonNull(currency, "Currency is required");

        if (openingBalance.signum() < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative"
            );
        }

        Instant now = Instant.now();

        return new Account(
                UUID.randomUUID(),
                accountNumber,
                customerName,
                email,
                openingBalance,
                currency,
                AccountStatus.ACTIVE,
                now,
                now
        );
    }

    public UUID getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }


    public static Account reconstitute(
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
        return new Account(
                id,
                accountNumber,
                customerName,
                email,
                balance,
                currency,
                status,
                createdAt,
                updatedAt
        );
    }
}
