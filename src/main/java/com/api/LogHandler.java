package com.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

public final class LogHandler extends Formatter {
    private static final Logger LOGGER = Logger.getLogger("com.api.logToFile");
    private static final String LOG_PATH = "%t/tidecli_log/mylog.txt"; //TODO: remove subfolder

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
        if (record.getLevel() == Level.INFO) {
            message.append("[").append(date.format(record.getMillis())).append("] ");
            message.append("[INFO] - ").append(record.getMessage()).append("\r\n").append("\r\n");
            return message.toString();
        }
        return "";
    }

    /**
     * This method is used to log general info such as "ApiHandler.submitExercise() called
     * with parameters: ....".
     * @param message Info message that you want to see in your log file.
     */
    public static void logInfo(String message) {
        try {
            Handler fh = new FileHandler(LOG_PATH);
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
            fh.setFormatter(new LogHandler());
            LOGGER.info(message);
        } catch (IOException e) {
            com.views.InfoView.displayError("Couldn't create or write to log file!", "Logging error");
            throw new RuntimeException(e);
        }
    }
}
