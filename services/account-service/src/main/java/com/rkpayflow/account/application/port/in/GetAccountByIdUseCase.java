package com.rkpayflow.account.application.port.in;

import com.rkpayflow.account.domain.Account;

import java.util.UUID;

public interface GetAccountByIdUseCase {

    Account execute(UUID accountId);
}