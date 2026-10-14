package com.rkpayflow.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

    boolean existsByAccountNumber(String accountNumber);

}
