package com.util;

import com.api.JsonHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public final class Config {
    private static final JsonObject CONFIG;
    private Config() { }
    static {
        try (InputStream is = JsonHandler.class.getClassLoader().getResourceAsStream("config.json")) {
            if (is == null) {
                throw new RuntimeException("config.json not found in resources");
            }
            CONFIG = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.json", e);
        }
    }

    /**
     * Gets a string from config.
     * @param name Name of the parameter.
     * @return Value of the parameter.
     */
    public static String getString(String name) {
        return CONFIG.has(name) ? CONFIG.get(name).getAsString() : null;
    }

    /**
     * Gets an in from config.
     * @param name Name of the parameter.
     * @return Value of the parameter.
     */
    public static int getInt(String name) {
        return CONFIG.has(name) ? CONFIG.get(name).getAsInt() : 0;
    }

    /**
     * Gets a boolean value from config.
     * @param name Name of the parameter.
     * @return Value of the parameter.
     */
    public static boolean getBoolean(String name) {
        return CONFIG.has(name) ? CONFIG.get(name).getAsBoolean() : false;
    }
}
