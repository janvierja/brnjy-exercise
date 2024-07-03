package com.barrenjoey.java.bank;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Stub {
    public static void main(String[] args) {

        String monitoredPath = "C:\\Users\\janvier\\Downloads\\txnLogs";
        String processedPath = "C:\\Users\\janvier\\Downloads\\completedTxnLogs";

        TransactionLoader loader = new TransactionLoader(monitoredPath, processedPath);
        BankImpl bank = new BankImpl(loader.getOutputQueue());
        bank.setReportingServer(new LegacyReportingServer());


        // Manual trigger processing
        // ------------------
        //loader.startProcessing();
        //bank.startProcesing();


        //try {
        //    int acct = 202;
        //    BankAccount account = bank.getAccountById(acct);
        //    for(AccountEntry e : account.entries()) {
        //        System.out.println(e);
        //    }
        //    System.out.println("ACCOUNT BALANCE (" + acct + "): " + account.getBalance());
        //} catch (AccountNotFoundException e) {
        //    System.out.println(e.getMessage());
        //}


        // Automatic processing

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Register a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cleaning up resources before shutdown...");
            bank.exitProc();
            loader.exitProc();
            executor.shutdown();
        }));


        executor.execute(bank);
        executor.execute(loader);

        while (!executor.isTerminated()) {
        }

        System.out.println("Program terminted.");
    }
}