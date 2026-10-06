package com.smarthostel.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Loads optional local values from .env without adding a third-party dependency. */
public final class EnvConfig {
    private static final Map<String, String> FILE_VALUES = new HashMap<>();
    private static boolean loaded;

    private EnvConfig() { }

    public static synchronized void load() {
        if (loaded) return;
        loaded = true;
        Path envFile = Path.of(".env");
        if (!Files.isRegularFile(envFile)) return;

        try {
            for (String line : Files.readAllLines(envFile)) {
                String value = line.trim();
                if (value.isEmpty() || value.startsWith("#")) continue;
                int separator = value.indexOf('=');
                if (separator <= 0) continue;
                String key = value.substring(0, separator).trim();
                String setting = value.substring(separator + 1).trim();
                if ((setting.startsWith("\"") && setting.endsWith("\"")) ||
                    (setting.startsWith("'") && setting.endsWith("'"))) {
                    setting = setting.substring(1, setting.length() - 1);
                }
                FILE_VALUES.put(key, setting);
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read .env: " + e.getMessage());
        }
    }

    /** System environment values take priority for deployment safety. */
    public static String get(String key) {
        load();
        String environmentValue = System.getenv(key);
        return environmentValue == null || environmentValue.isBlank()
                ? FILE_VALUES.getOrDefault(key, "")
                : environmentValue;
    }
}
