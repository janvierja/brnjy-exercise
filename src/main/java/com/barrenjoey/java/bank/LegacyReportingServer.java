package com.barrenjoey.java.bank;

import java.time.Instant;

public class LegacyReportingServer implements ReportingServer{

    /**
     * I'm sorry - you cannot change this method :)
     *
     */
    @Override
    public void reportActivity(int accountId, Instant instant, double amount, double balance) {
        //assume this code cannot change. i.e. you are stuck with some legacy component, it's performance sucks.
        // It holds the calling thread up for 250 ms.
        //
        // You'll need to find away to meet the requirements without changing the code in here
        try {
            System.out.println(":: " + Thread.currentThread().getId() + " :: (" + accountId + ") " + instant + "  $" + amount + ", account balance: " + balance);
            Thread.sleep(250);
        } catch (InterruptedException ignore) {}
    }
}
