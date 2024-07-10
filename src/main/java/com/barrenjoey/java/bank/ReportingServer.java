package com.barrenjoey.java.bank;

import java.time.Instant;

public interface ReportingServer {

    /**
     * Send account activity to some server used for reporting purposes. This method is thread safe.
     *
     * @param accountId
     * @param instant
     * @param amount
     * @param balance The balance of the account post application of the activity
     */
    void reportActivity(int accountId, Instant instant, double amount, double balance);
}
