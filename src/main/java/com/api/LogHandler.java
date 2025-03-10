package com.api;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {
    private static final Logger LOGGER = Logger.getLogger("com.api.logToFile");
    public static void logToFile(String level, String message) {
        try {
            Handler fh = new FileHandler("%t/tidecli_log/mylog.txt");
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
            switch (level) {
                case "debug":
                    LOGGER.log(Level.INFO, message);
                    break;
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
