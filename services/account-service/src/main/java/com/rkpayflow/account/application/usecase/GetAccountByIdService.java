package com.rkpayflow.account.application.usecase;

import com.rkpayflow.account.application.exception.AccountNotFoundException;
import com.rkpayflow.account.application.port.AccountRepositoryPort;
import com.rkpayflow.account.application.port.in.GetAccountByIdUseCase;
import com.rkpayflow.account.domain.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAccountByIdService implements GetAccountByIdUseCase {

    private final AccountRepositoryPort accountRepository;

    public GetAccountByIdService(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}