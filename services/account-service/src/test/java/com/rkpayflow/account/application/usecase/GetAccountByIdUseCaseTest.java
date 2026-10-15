package com.rkpayflow.account.application.usecase;

import com.rkpayflow.account.application.exception.AccountNotFoundException;
import com.rkpayflow.account.application.port.AccountRepositoryPort;
import com.rkpayflow.account.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAccountByIdUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private GetAccountByIdService getAccountByIdUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepositoryPort.class);
        getAccountByIdUseCase = new GetAccountByIdService(accountRepository);
    }

    @Test
    void shouldReturnAccountWhenItExists() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.open(
                "RK1001",
                "Ronald Kagande",
                "ronald@example.com",
                new BigDecimal("100.00"),
                Currency.getInstance("ZAR"));

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        Account result = getAccountByIdUseCase.execute(accountId);

        assertSame(account, result);
        verify(accountRepository).findById(accountId);
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> getAccountByIdUseCase.execute(accountId));

        verify(accountRepository).findById(accountId);
    }
}
