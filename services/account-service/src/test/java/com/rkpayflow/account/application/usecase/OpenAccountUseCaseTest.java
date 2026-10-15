package com.rkpayflow.account.application.usecase;

import com.rkpayflow.account.application.exception.DuplicateAccountNumberException;
import com.rkpayflow.account.application.port.AccountRepositoryPort;
import com.rkpayflow.account.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAccountUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private OpenAccountService openAccountUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepositoryPort.class);
        openAccountUseCase = new OpenAccountService(accountRepository);
    }

    @Test
    void shouldOpenAndSaveAccount() {
        when(accountRepository.existsByAccountNumber("RK1001"))
                .thenReturn(false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account account = openAccountUseCase.execute(
                "RK1001",
                "Ronald Kagande",
                "ronald@example.com",
                new BigDecimal("100.00"),
                Currency.getInstance("ZAR"));

        assertNotNull(account);
        assertEquals("RK1001", account.getAccountNumber());
        assertEquals(new BigDecimal("100.00"), account.getBalance());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldRejectDuplicateAccountNumber() {
        when(accountRepository.existsByAccountNumber("RK1001"))
                .thenReturn(true);

        assertThrows(DuplicateAccountNumberException.class, () ->
                openAccountUseCase.execute(
                        "RK1001",
                        "Ronald Kagande",
                        "ronald@example.com",
                        new BigDecimal("100.00"),
                        Currency.getInstance("ZAR")));

        verify(accountRepository, never()).save(any(Account.class));
    }
}
