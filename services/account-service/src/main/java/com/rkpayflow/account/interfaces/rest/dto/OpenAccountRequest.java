package com.rkpayflow.account.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OpenAccountRequest(

        @NotBlank(message = "Account number is required")
        @Size(max = 20, message = "Account number must not exceed 20 characters")
        String accountNumber,

        @NotBlank(message = "Customer name is required")
        @Size(max = 150, message = "Customer name must not exceed 150 characters")
        String customerName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotNull(message = "Opening balance is required")
        @DecimalMin(value = "0.00", message = "Opening balance cannot be negative")
        BigDecimal openingBalance,

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter uppercase code")
        String currency

) {
}
