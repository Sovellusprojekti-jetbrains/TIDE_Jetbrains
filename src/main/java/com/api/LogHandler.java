package com.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

/**
 * This class contains methods for logging with custom style entries.
 * Use examples:
 * ===============================
 * call logInfo method E.g. at the start and end of method you are inspecting,
 * with descriptive messages
 * E.g. com.api.LogHandler.logInfo(ApiHandler.isLoggedIn() called);
 * E.g. com.api.LogHandler.logInfo(ApiHandler.isLoggedIn() returned ...);
 * ===============================
 * Call logDebug method somewhere in the method you are inspecting,
 * by giving lists of variable names and corresponding values
 * E.g. com.api.LogHandler.logDebug(new String[]{String name}, newString[]{name});
 * ===============================
 * Call logError method in catch clauses with method's name you are inspecting,
 * and Exception
 * E.g. com.api.LogHandler.logError("Action listener of avaaTehtava", exception);
 * ===============================
 * Example output:
 * [2025-03-11 15:56:21:470] [INFO] - ApiHandler.isLoggedIn() called
 *     [DEBUG] - Variable names and values:
 *         String jsonOutput = foobar
 * [2025-03-11 15:56:23:587] [INFO] - ApiHandler.isLoggedIn() returned true
 * ===============================
 * [2025-03-11 16:45:31:005] [ERROR] [SEVERITY:900]
 *     Method: Action listener of avaaTehtava
 *     Failed to open ???. Error message: Määritettyä tiedostoa ei löydy.
 *     Stack trace:
 *         .....
 */
public final class LogHandler extends Formatter {
    private static final Logger LOGGER = Logger.getLogger("com.api.logToFile");
    private static final String LOG_PATH = "%t/tide-cli_log.txt";
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
        if (record.getLevel() == Level.WARNING) {
            message.append("[").append(date.format(record.getMillis()))
                    .append("] ").append("[ERROR] [SEVERITY:").append(Level.WARNING.intValue())
                    .append("]").append(System.lineSeparator());
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
            fh.setFormatter(new LogHandler()); //comment this if you fancy java's default logging style
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
                case "error":
                    LOGGER.warning(message);
                    break;
                default:
                    LOGGER.warning("Unknown logging level. Debug LogHandler.java's internal method calls\r\n");
            }
        } catch (IOException e) {
            com.views.InfoView.displayError("Couldn't create or write to log file!");
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

    /**
     * This method is used to log error details for example when exception is caught.
     * @param methodName Name of the class and method, Eg. ApiHandler.submitExercise().
     * @param e Exception that was caught.
     */
    public static void logError(String methodName, Exception e) {
        StringBuilder error = new StringBuilder();
        StackTraceElement[] stack = e.getStackTrace();
        error.append("\t").append("Method: ").append(methodName)
                .append(System.lineSeparator()).append(System.lineSeparator());
        error.append("\t").append(e.getMessage()).append(System.lineSeparator());
        error.append("\t").append("Stack trace:").append(System.lineSeparator());
        for (StackTraceElement se : stack) {
            error.append("\t\t").append(se.toString()).append(System.lineSeparator());
        }
        error.append(System.lineSeparator());
        log(error.toString(), "error");
    }
}
