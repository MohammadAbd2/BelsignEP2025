package BLL.LoggerBLL;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class LogAnalyzer {

    public static List<String> getOrdersAddedInLastWeek() {
        List<String> recentOrders = new ArrayList<>();

        // Use a relative path to the log file
        Path logFilePath = Paths.get("DAL", "QC_Belsign.log");
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        // If the log file does not exist, return an empty list
        if (!Files.exists(logFilePath)) {
            System.out.println("Log file does not exist.");
            return recentOrders;
        }

        try (BufferedReader reader = Files.newBufferedReader(logFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ORDER_ADDED")) {
                    // Extract timestamp from the log line
                    String[] parts = line.split("\\|");
                    String timePart = parts[parts.length - 1].trim(); // "Time: 2025-05-01T..."
                    LocalDateTime logTime = LocalDateTime.parse(timePart.split("Time: ")[1]);

                    if (logTime.isAfter(oneWeekAgo)) {
                        recentOrders.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recentOrders;
    }

    public static void RegisterLog(String log, int alertType) {
        if (alertType < 1 || alertType > 3) {
            System.out.println("You need to select a valid alert type!!");
            return;
        }

        if (log == null || log.isEmpty()) {
            System.out.println("Log should not be empty!");
            return;
        }

        switch (alertType) {
            case 1:
                loggerManager.logInfo(log);
                System.out.println("Register Info Log: " + log);
                break;
            case 2:
                loggerManager.logWarning(log);
                System.out.println("Register Warning Log: " + log);
                break;
            case 3:
                loggerManager.logError(log);
                System.out.println("Register Error Log: " + log);
                break;
        }
    }


    public static List<String> getLogsByType(String logType) {
        List<String> logs = new ArrayList<>();
        Path logFilePath = Paths.get("logs", "QC_Belsign.log");

        if (!Files.exists(logFilePath)) {
            System.out.println("Log file does not exist.");
            return logs;
        }

        try (BufferedReader reader = Files.newBufferedReader(logFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(logType)) {
                    logs.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static List<String> getInfoLogs() {
        return getLogsByType("INFO");
    }

    public static List<String> getWarningLogs() {
        return getLogsByType("WARNING");
    }

    public static List<String> getErrorLogs() {
        return getLogsByType("ERROR");
    }
}
