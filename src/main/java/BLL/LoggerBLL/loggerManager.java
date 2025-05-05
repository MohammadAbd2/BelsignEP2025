package BLL.LoggerBLL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class loggerManager {
    private static final Logger logger;

    static {
        logger = Logger.getLogger("QC_BelsignLogger");

        try {
            // Create DAL folder if it doesn't exist (relative to project root)
            String folderName = "logs";
            File dir = new File(folderName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Create or append to the log file inside DAL folder
            String logPath = Paths.get(folderName, "QC_Belsign.log").toString();
            FileHandler fh = new FileHandler(logPath, true);
            fh.setFormatter(new SimpleFormatter());

            logger.addHandler(fh);
            logger.setUseParentHandlers(false); // Prevent logs from printing to console
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logError(String message) {
        logger.severe(message);
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
