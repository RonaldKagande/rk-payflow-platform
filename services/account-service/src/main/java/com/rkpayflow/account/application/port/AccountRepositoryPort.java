package com.rkpayflow.account.application.port;

import com.rkpayflow.account.domain.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

    Account save(Account account);

    Optional<Account> findById(UUID id);

    boolean existsByAccountNumber(String accountNumber);
}
