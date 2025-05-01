package BLL;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class logAnalyzer {

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
}
