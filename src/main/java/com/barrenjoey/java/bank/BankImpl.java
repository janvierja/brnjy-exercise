package com.barrenjoey.java.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BankImpl implements Bank, Runnable {

    private static final Map<Integer, BankAccountImpl> accounts = new ConcurrentHashMap<>();

    private BlockingQueue<AccountEntry> inputQueue;

    private static final int POOL_COUNT = 500;
    private static final ExecutorService executor = Executors.newFixedThreadPool(POOL_COUNT);
    private static ReportingServer reportingServer;

    private static boolean running = true;

    public BankImpl(BlockingQueue<AccountEntry> queue) {
        setInput(queue);
    }

    public void setInput(BlockingQueue<AccountEntry> inputQueue) {
        this.inputQueue = inputQueue;
    }
    
    public void setReportingServer(ReportingServer reportingServer) {
        BankImpl.reportingServer = reportingServer;
    }

    private static void process(AccountEntry entry) {

        int accountId = entry.getAccountId();
        double amount = entry.getAmount();
        AccountActionEnum action = entry.getAction();

        BankAccountImpl account = accounts.get(accountId);
        if(account == null) {
            account = new BankAccountImpl(accountId, 0.0);
        }

        double balance = 0.0;
        switch (action) {
            case DEPOSIT:
                balance = account.deposit(amount);
                break;
            case WITHDRAW:
                balance = account.withdraw(amount);
                break;
            case UNKNOWN:
                break;
        }

        double runningBalance = balance;
        Runnable reportingTask = () -> {
            reportingServer.reportActivity(accountId, entry.getEntryDateAndTime(), amount, runningBalance);
        };

        executor.execute(reportingTask);

        accounts.putIfAbsent(accountId, account);
    }

    // For manually triggered execution
    public void startProcesing() {
        List<AccountEntry> list = new ArrayList<>();
        this.inputQueue.drainTo(list);

        list.forEach(BankImpl::process);

        executor.shutdown();
    }

    public void exitProc() {
        executor.shutdown();
        running = false;
    }

    @Override
    public BankAccount getAccountById(int accountId) throws AccountNotFoundException {
        BankAccountImpl account = accounts.get(accountId);
        if(account == null) {
            throw new AccountNotFoundException("Account not found!");
        }
        return account;
    }

    @Override
    public void run() {
        while(running) {
            try {
                AccountEntry accountEntry = this.inputQueue.take();
                process(accountEntry);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
