package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.BankAccountDto;
import com.eteration.simplebanking.exception.AccountNotFoundException;
import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// This class is a place holder you can change the complete implementation
@RestController
@RequestMapping("/account/v1")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;


    @GetMapping
    public ResponseEntity<List<BankAccountDto>> getAllAccount() {
        try {
            List<BankAccountDto> accountDto = bankAccountService.getAll();
            return ResponseEntity.ok(accountDto);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        try {
            BankAccount account = bankAccountService.findAccount(accountNumber);
            return ResponseEntity.ok(account);

        } catch (AccountNotFoundException ex) {
            String errorMessage = "Hesap bulunamadı: " + accountNumber;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody BankAccountDto bankAccountDto) {
        try {
            if (bankAccountDto.getOwner() == null || bankAccountDto.getOwner().trim().isEmpty()) {
                throw new IllegalArgumentException("Hesap bilgileri boş olamaz");
            }
            BankAccount newAccount = new BankAccount();
            newAccount.setAccountNumber(bankAccountDto.getAccountNumber());
            newAccount.setOwner(bankAccountDto.getOwner());

            BankAccount savedAccount = bankAccountService.saveAccount(newAccount);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
        } catch (IllegalArgumentException illegalEx) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(illegalEx.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }

    }


    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber, @RequestBody DepositTransaction depositTransaction) throws InsufficientBalanceException {

        TransactionStatus status = bankAccountService.credit(accountNumber, depositTransaction.getAmount());
        return ResponseEntity.ok(status);

    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody WithdrawalTransaction withdrawalTransaction) throws InsufficientBalanceException {

        TransactionStatus status = bankAccountService.debit(accountNumber, withdrawalTransaction.getAmount());
        return ResponseEntity.ok(status);

    }
}