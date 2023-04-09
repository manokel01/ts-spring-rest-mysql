package tinysensormanager.service.util;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The LoggerUtil class provides a utility method to obtain the current logger object for logging application events.
 *  The logger object is initialized with a bridge handler to send the logs to SLF4J and a file handler to store them in
 *  a log file
 *
 * @author manokel01
 * @version 1.0.0
 */
public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    /**
     * Initializes the logger object with a bridge handler to send the
     * logs to SLF4J and a file handler to store them in a log file.
     */
    static {
        SLF4JBridgeHandler.install(); // Installs the bridge (JUL-to_SLF) Handler
        Handler fileHandler;
        try {
            fileHandler = new FileHandler("logfile.log", true);  // true for update the file
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.addHandler(fileHandler);
    }

    /**
     * Returns the current logger object for logging application events.
     * @return the current logger object
     */
    private LoggerUtil() {}

    public static Logger getCurrentLogger() {
        return logger;
    }
}
