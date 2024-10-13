package com.eteration.simplebanking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {

    private String accountNumber;
    private String owner;
    private double balance;
}
