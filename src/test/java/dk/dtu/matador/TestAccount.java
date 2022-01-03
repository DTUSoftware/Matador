package dk.dtu.matador;

import dk.dtu.matador.objects.Account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAccount {
    @Test
    public void testAccount() {
        Account account = new Account();

        assertEquals(0, account.getBalance());

        assertFalse(account.withdraw(100));
        assertEquals(0, account.getBalance());

        account.deposit(100);
        assertEquals(100, account.getBalance());
        assertEquals("100.0", account.getBalanceString());

        assertTrue(account.withdraw(100));
        assertEquals(0, account.getBalance());

        double bal = 1000000000000.0;
        account.setBalance(bal);
        assertEquals(bal, account.getBalance());
        assertEquals("1.0E12", account.getBalanceString());
    }
}
