package com.barrenjoey.java.bank;

import org.apache.commons.math3.util.Precision;

import java.util.TreeSet;

public class BankAccountImpl implements BankAccount {

    private final int accountId;
    private double balance;

    private final TreeSet<AccountEntry> transactions = new TreeSet<>(AccountEntry.sortByTimestamp);

    public BankAccountImpl(int acct, double startingBalance) {
        this.accountId = acct;
        this.balance = startingBalance;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public AccountEntry[] entries() {
        return transactions.toArray(new AccountEntry[0]);
    }

    private double apply(double deltaAmount) {
        this.balance = Precision.round((this.balance + deltaAmount), 2);
        return this.balance;
    }

    public double deposit(double depositAmount) {
        if(depositAmount < 0) {
            throw new IllegalArgumentException("depositAmount must be positive");
        }

        transactions.add(new AccountEntry(this.accountId, depositAmount, AccountActionEnum.DEPOSIT));

        return apply(depositAmount);
    }

    public double withdraw(double withdrawalAmount) {
        if(withdrawalAmount < 0) {
            throw new IllegalArgumentException("withdrawalAmount must be positive");
        }

        transactions.add(new AccountEntry(this.accountId, withdrawalAmount, AccountActionEnum.WITHDRAW));

        return apply(withdrawalAmount * -1);
    }

    @Override
    public String toString() {
        return "BankAccountImpl{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }
}
