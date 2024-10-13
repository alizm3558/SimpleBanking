package com.eteration.simplebanking;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.PhoneBillPaymentTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void testTransactions() throws InsufficientBalanceException {
        BankAccount account = new BankAccount("Jim", "12345");
        final double delta = 0.0001;

        //Para yatırma
        account.post(new DepositTransaction(1000));
        assertEquals(1000, account.getBalance(), delta);

        //Para çekme
        account.post(new WithdrawalTransaction(200));
        assertEquals(800, account.getBalance(), delta);

        //Telefon faturası ödeme
        account.post(new PhoneBillPaymentTransaction("Vodafone", "5423345566", 96.50));
        assertEquals(703.50, account.getBalance(), delta);
    }



    @Test
    public void testCredit() {
        // Yeni bir hesap oluştur
        BankAccount account = new BankAccount("Ali Bakir", "12345");

        // Hesaba 1000 birim yatır ve bakiyeyi kontrol et
        account.credit(1000.0);
        assertEquals(1000.0, account.getBalance(), 0.001);  // Bakiye 1000.0 olmalı
    }

}
