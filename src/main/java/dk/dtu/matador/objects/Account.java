package dk.dtu.matador.objects;

import java.util.UUID;

/**
 * The Account class, typically a Player has an instance of this class.
 */
public class Account {
    private final UUID accountID;
    private double balance = 0.0;

    public Account() {
        this.accountID = UUID.randomUUID();
    }

    public Account(double startingBalance) {
        this.accountID = UUID.randomUUID();
        balance = startingBalance;
    }

    /**
     * Returns the balance as a rounded string.
     *
     * @return  The balance as a String, rounded.
     */
    public String getBalanceString() {
        return Float.toString(Math.round(balance));
    }

    /**
     * Gets money.
     *
     * @return      A double with the given account balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Withdraws or deposits a certain amount of cash onto the account.
     *
     * @param amount    The amount to deposit/withdraw.
     * @return          <code>true</code> if the transaction succeeded,
     *                  <code>false</code> if the amount didn't have a sufficient balance.
     */
    private boolean doAccountWithdraw(double amount) {
        if (balance+amount < 0) {
            return false;
        }
        balance = balance+amount;
        return true;
    }

    /**
     * Withdraws a certain amount of cash onto the account.
     *
     * @param amount    The amount to withdraw.
     * @return          <code>true</code> if the transaction succeeded,
     *                  <code>false</code> if the amount didn't have a sufficient balance.
     */
    public boolean withdraw(double amount) {
        return doAccountWithdraw(-amount);
    }

    /**
     * Deposits a certain amount of cash onto the account.
     *
     * @param amount    The amount to deposit.
     */
    public void deposit(double amount) {
        doAccountWithdraw(amount);
    }

    /**
     * Sets the balance to the given amount.
     *
     * @param balance   The balance to set to.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UUID getID() {
        return accountID;
    }
}
