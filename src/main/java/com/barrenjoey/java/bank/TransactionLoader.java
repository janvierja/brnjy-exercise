package com.barrenjoey.java.bank;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionLoader implements Runnable {

    private static final int ACCOUNT_IDX = 0;
    private static final int ACTION_IDX = 1;
    private static final int AMOUNT_IDX = 2;
    private static final long READ_DELAY = 1000; // configurable

    private static final BlockingQueue<AccountEntry> outputQueue = new LinkedBlockingQueue<>();
    private static final List<String> rejected = new ArrayList<>();

    private Path monitoredPath;
    private static Path processedPath;

    private static boolean running = true;

    public TransactionLoader(String path, String processedPath) {
        setMonitoredPath(path);
        setProcessedPath(processedPath);
    }

    private void publish(AccountEntry entry) {
        try {
            outputQueue.put(entry);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getMonitoredPath() {
        return monitoredPath;
    }

    public void setMonitoredPath(String p) {
        monitoredPath = Paths.get(p);
    }

    public Path getProcessedPath() {
        return processedPath;
    }

    public void setProcessedPath(String p) {
        processedPath = Paths.get(p);
    }

    public void exitProc() {
        running = false;
    }

    public BlockingQueue<AccountEntry> getOutputQueue() {
        return outputQueue;
    }

    private static void moveToCompletedDir(Path sourcePath) {
        Path targetPath = Paths.get(processedPath.toString(),
                sourcePath.getFileName().toString());
        System.out.println("Moving from " + sourcePath + " to " + targetPath);
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int rejectCount = 0;
            int acceptedCount = 0;
            while ((line = br.readLine()) != null) {
                String[] tokens = StringUtils.split(line, ",");
                try {

                    int accountId = Integer.parseInt(tokens[ACCOUNT_IDX]);
                    AccountActionEnum action = AccountActionEnum.decode(tokens[ACTION_IDX]);
                    double amount = Double.parseDouble(tokens[AMOUNT_IDX]);

                    AccountEntry entry = new AccountEntry(accountId, amount, action);

                    acceptedCount++;
                    outputQueue.put(entry);

                } catch (Exception e) {
                    rejectCount++;
                    rejected.add(line);
                }
            }
            System.out.println("----\nSUMMARY:\n\t" + acceptedCount + " record(s) accepted.\n\t" + rejectCount + " record(s) rejected.\n----");
        }
    }

    private static void process(Path target) {
        try {
            System.out.println("Processing: " + target);
            FileInputStream fileInputStream = new FileInputStream(target.toString());
            load(fileInputStream);
            fileInputStream.close();
            moveToCompletedDir(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startProcessing() {
        startProcessing(monitoredPath);
    }

    public void startProcessing(Path txnSourcePath) {
        try (Stream<Path> paths = Files.list(txnSourcePath)) {
            paths
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparingInt(path ->
                        Integer.parseInt(path.getFileName().toString().replaceAll("\\D", ""))))
                .collect(Collectors.toList()).forEach(TransactionLoader::process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Monitoring directory: " + monitoredPath);
        while (running) {
            try {
                // Delay processing by `readDelay` milliseconds
                // This allows users to complete copying the files into the monitored path
                TimeUnit.MILLISECONDS.sleep(READ_DELAY);
                startProcessing(monitoredPath);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
