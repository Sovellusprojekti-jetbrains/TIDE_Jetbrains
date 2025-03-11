package com.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

public final class LogHandler extends Formatter {
    private static final Logger LOGGER = Logger.getLogger("com.api.logToFile");
    private static final String LOG_PATH = "%t/tidecli_log/mylog.txt"; //TODO: remove subfolder
    private static FileHandler fh = null;

    private LogHandler() {
    }

    /**
     * This method is used to create custom log entries.
     * @param record the log record to be formatted.
     * @return Custom log entry.
     */
    @Override
    public String format(LogRecord record) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        StringBuilder message = new StringBuilder();
        if (record.getLevel() == Level.FINE) {
            message.append("[").append(date.format(record.getMillis())).append("] ");
            message.append("[INFO] - ").append(record.getMessage()).append(System.lineSeparator()).append(System.lineSeparator());
            return message.toString();
        }
        if (record.getLevel() == Level.INFO) {
            message.append("\t").append("[DEBUG] - Variable names and values:").append(System.lineSeparator());
            message.append(record.getMessage());
            return message.toString();
        }
        return "";
    }

    /**
     * Initializes FileHandler which writes the log file.
     * @throws IOException If file read/write errors occur.
     */
    private static void initHandler() throws IOException {
        if (fh == null) {
            fh = new FileHandler(LOG_PATH, true);
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
            fh.setFormatter(new LogHandler());
        }
    }

    /**
     * Private method to do the actual logging.
     * @param message Log message.
     * @param level Determines the style of custom entry.
     */
    private static void log(String message, String level) {
        try {
            initHandler();
            switch (level) {
                case "info":
                    LOGGER.fine(message);
                    break;
                case "debug":
                    LOGGER.info(message);
                    break;
                default:
                    LOGGER.warning("Check LogHandler.java internal configuration");
            }
        } catch (IOException e) {
            com.views.InfoView.displayError("Couldn't create or write to log file!", "Logging error");
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to log general info such as "ApiHandler.submitExercise() called
     * with parameters: ....".
     * @param message Info message that you want to see in the log file.
     */
    public static void logInfo(String message) {
        log(message, "info");
    }

    /**
     * This method is used to log debug information about variables and their values.
     * @param varNames List of variable names you want to see in the log file.
     * @param varValues List of variable values you want to see in the log file.
     */
    public static void logDebug(String[] varNames, String[] varValues) {
        StringBuilder list = new StringBuilder();
        if (varValues.length >= varNames.length) {
            for (int i = 0; i < varNames.length; i++) {
                list.append("\t\t").append(varNames[i]).append(" = ").append(varValues[i])
                        .append(System.lineSeparator());
            }
        } else {
            list.append("\t\t").append("Incorrect number of varNames and varValues given");
        }
        list.append(System.lineSeparator());
        log(list.toString(), "debug");
    }
}
