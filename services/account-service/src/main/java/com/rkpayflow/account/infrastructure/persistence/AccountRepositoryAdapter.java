package com.rkpayflow.account.infrastructure.persistence;

import com.rkpayflow.account.application.port.AccountRepositoryPort;
import com.rkpayflow.account.domain.Account;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Account save(Account account) {
        AccountEntity entity = toEntity(account);
        AccountEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(AccountRepositoryAdapter::toDomain);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpaRepository.existsByAccountNumber(accountNumber);
    }

    private static AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.getId());
        entity.setAccountNumber(account.getAccountNumber());
        entity.setCustomerName(account.getCustomerName());
        entity.setEmail(account.getEmail());
        entity.setBalance(account.getBalance());
        entity.setCurrency(account.getCurrency().getCurrencyCode());
        entity.setStatus(account.getStatus());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());
        return entity;
    }

    private static Account toDomain(AccountEntity entity) {
        return Account.reconstitute(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getCustomerName(),
                entity.getEmail(),
                entity.getBalance(),
                Currency.getInstance(entity.getCurrency()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
