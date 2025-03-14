package backend343.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSingleton {
    private static LoggerSingleton instance;
    private final Logger logger;

    private LoggerSingleton() {
        this.logger = LoggerFactory.getLogger(LoggerSingleton.class);
    }

    public static LoggerSingleton getInstance() {
        if (instance == null) {
            synchronized (LoggerSingleton.class) { // Thread-safe initialization bc of synchronized
                if (instance == null) {
                    instance = new LoggerSingleton();
                }
            }
        }
        return instance;
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.error(message);
    }
}