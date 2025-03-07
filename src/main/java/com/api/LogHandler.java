package com.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandler {
    private static Logger logger = LoggerFactory.getLogger(LogHandler.class);
    public static void logToFile(String level, String message) {
        switch (level) {
            case "info":
                logger.info(message);
                break;
            case "debug":
                logger.debug(message);
                break;
            case "error":
                logger.error(message);
                break;
            default:
                System.err.println("Unknown logging level");
        }
    }
}
