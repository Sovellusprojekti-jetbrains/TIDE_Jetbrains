package com.api;

import java.io.IOException;
import java.util.logging.*;

public final class LogHandler {
    private static final Logger LOGGER = Logger.getLogger("com.api.logToFile");
    private static final String LOG_PATH = "%t/tidecli_log/mylog.txt";

    private LogHandler() {
    }

    /**
     * This method is used for logging info and errors to the log file.
     * @param level Level of detail. Message output can be varied according to situation.
     * @param message Strings containing message to be written to the log.
     */
    public static void logToFile(String level, String... message) {
        try {
            Handler fh = new FileHandler(LOG_PATH);
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
            switch (level) {
                case "info":
                    fh.setFormatter(new SimpleFormatter());
                    LOGGER.log(Level.INFO, message[0]);
                    break; //TODO: debug taso
                case "error":
                    //logger.error(message);
                    break;
                default:
                    System.err.println("Unknown logging level");
            }
        } catch (IOException e) {
            com.views.InfoView.displayError("Couldn't create or write to log file!", "Logging error");
            throw new RuntimeException(e);
        }
    }
}
