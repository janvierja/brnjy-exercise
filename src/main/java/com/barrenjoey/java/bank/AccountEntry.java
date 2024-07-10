package com.barrenjoey.java.bank;

import java.time.Instant;
import java.util.Comparator;

public class AccountEntry {
    private int accountId;
    private double amount;
    private AccountActionEnum action;

    private Instant entryDateAndTime;
    private Long timestamp; // only used for sorting

    public AccountEntry(int accountId, double amount, AccountActionEnum action) {
        this.accountId = accountId;
        this.amount = amount;
        this.action = action;

        this.entryDateAndTime = Instant.now();
        this.timestamp = System.nanoTime();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AccountActionEnum getAction() {
        return action;
    }

    public void setAction(AccountActionEnum action) {
        this.action = action;
    }

    public Instant getEntryDateAndTime() {
        return entryDateAndTime;
    }

    public static final Comparator<AccountEntry> sortByTimestamp =
            (AccountEntry e1, AccountEntry e2) -> (e1.timestamp.compareTo(e2.timestamp));

    @Override
    public String toString() {
        return "AccountEntry{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                ", action=" + action +
                ", entryDateAndTime=" + entryDateAndTime +
                ", timestamp=" + timestamp +
                '}';
    }
}
