package com.rkpayflow.account.interfaces.rest;

import com.rkpayflow.account.application.port.in.GetAccountByIdUseCase;
import com.rkpayflow.account.application.port.in.OpenAccountUseCase;
import com.rkpayflow.account.domain.Account;
import com.rkpayflow.account.interfaces.rest.dto.AccountResponse;
import com.rkpayflow.account.interfaces.rest.dto.OpenAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final OpenAccountUseCase openAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public AccountController(
            OpenAccountUseCase openAccountUseCase,
            GetAccountByIdUseCase getAccountByIdUseCase) {
        this.openAccountUseCase = openAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> openAccount(
            @Valid @RequestBody OpenAccountRequest request) {

        Account account = openAccountUseCase.execute(
                request.accountNumber(),
                request.customerName(),
                request.email(),
                request.openingBalance(),
                Currency.getInstance(request.currency()));

        AccountResponse response = AccountResponse.from(account);

        URI location = URI.create("/api/accounts/" + account.getId());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID id) {

        Account account = getAccountByIdUseCase.execute(id);

        return ResponseEntity.ok(AccountResponse.from(account));
    }
}
