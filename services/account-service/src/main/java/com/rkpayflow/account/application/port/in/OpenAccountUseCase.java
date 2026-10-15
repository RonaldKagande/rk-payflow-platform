package com.rkpayflow.account.application.port.in;

import com.rkpayflow.account.domain.Account;

import java.math.BigDecimal;
import java.util.Currency;

public interface OpenAccountUseCase {

    Account execute(
            String accountNumber,
            String customerName,
            String email,
            BigDecimal openingBalance,
            Currency currency);
}
