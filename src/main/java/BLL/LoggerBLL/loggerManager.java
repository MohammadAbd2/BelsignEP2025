package BLL.LoggerBLL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.*;

public class loggerManager {
    private static final Logger logger;

    static {
        logger = Logger.getLogger("QC_BelsignLogger");

        try {
            // Create DAL folder if it doesn't exist (relative to project root)
            String folderName = "DAL";
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
}
