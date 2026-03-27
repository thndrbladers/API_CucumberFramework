package com.api.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager that loads environment-specific properties.
 * Environment is resolved from: system property "env" → Maven profile → default "qa".
 */
public final class ConfigManager {

    private static final Logger LOG = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;
    private final String environment;

    private ConfigManager() {
        this.environment = resolveEnvironment();
        this.properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /** Resets the singleton — useful when switching environments in tests. */
    public static synchronized void reset() {
        instance = null;
    }

    public String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null) {
            return systemProp;
        }
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getAuthToken() {
        return getProperty("auth.token");
    }

    public int getConnectionTimeout() {
        return Integer.parseInt(getProperty("timeout.connection", "10000"));
    }

    public int getReadTimeout() {
        return Integer.parseInt(getProperty("timeout.read", "10000"));
    }

    public boolean shouldLogRequest() {
        return Boolean.parseBoolean(getProperty("log.request", "true"));
    }

    public boolean shouldLogResponse() {
        return Boolean.parseBoolean(getProperty("log.response", "true"));
    }

    public String getEnvironment() {
        return environment;
    }

    private String resolveEnvironment() {
        String env = System.getProperty("env");
        if (env == null || env.isBlank()) {
            env = System.getenv("ENV");
        }
        if (env == null || env.isBlank()) {
            env = "qa";
        }
        LOG.info("Resolved environment: {}", env);
        return env.toLowerCase();
    }

    private void loadProperties() {
        String fileName = "config/" + environment + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: " + fileName);
            }
            properties.load(input);
            LOG.info("Loaded configuration from: {}", fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + fileName, e);
        }
    }
}
