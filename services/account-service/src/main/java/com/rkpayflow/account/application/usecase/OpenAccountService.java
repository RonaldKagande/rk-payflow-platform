package com.rkpayflow.account.application.usecase;

import com.rkpayflow.account.application.exception.DuplicateAccountNumberException;
import com.rkpayflow.account.application.port.AccountRepositoryPort;
import com.rkpayflow.account.application.port.in.OpenAccountUseCase;
import com.rkpayflow.account.domain.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class OpenAccountService implements OpenAccountUseCase {

    private final AccountRepositoryPort accountRepository;

    public OpenAccountService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(
            String accountNumber,
            String customerName,
            String email,
            BigDecimal openingBalance,
            Currency currency) {

        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new DuplicateAccountNumberException(accountNumber);
        }

        Account account = Account.open(
                accountNumber,
                customerName,
                email,
                openingBalance,
                currency);

        return accountRepository.save(account);
    }
}