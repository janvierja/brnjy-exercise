package com.barrenjoey.java.bank;

public interface BankAccount {
    /**
     * Get the current balance of the account
     * @return the balance
     */
    double getBalance();

    /**
     * Return the individual account entries in the order they were applied
     * @return the entries
     */
    AccountEntry[] entries();
}
