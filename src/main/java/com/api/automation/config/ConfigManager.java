package com.api.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager.
 * Loads environment-specific .properties file from config/ folder.
 *
 * WHY Singleton: Only one instance exists across all tests. Every class that calls
 *   ConfigManager.getInstance() gets the same config — no duplicate file reads,
 *   no inconsistent state.
 *
 * BENEFIT: Switch environments with one flag (e.g., mvn test -Denv=dev) and every
 *   client/utility automatically picks up the correct base URL, tokens, timeouts, etc.
 *
 * Environment resolution order (first non-empty wins):
 *   1. -Denv=dev  (JVM system property, set via mvn test -Denv=dev)
 *   2. ENV environment variable  (set in CI/CD pipeline config)
 *   3. Defaults to "qa"
 *
 * Once resolved, loads config/{env}.properties (e.g., config/qa.properties).
 * All getters check system properties first, so -D flags can override any config value.
 *
 * Example:
 *   ConfigManager.getInstance().getBaseUrl()    → "https://jsonplaceholder.typicode.com"
 *   ConfigManager.getInstance().getAuthToken()  → "Bearer xyz" (if set in properties)
 */
public final class ConfigManager {

    private static final Logger LOG = LogManager.getLogger(ConfigManager.class);

    // Singleton instance — created once, reused everywhere.
    // WHY static: Shared across all threads/classes without passing it around.
    private static ConfigManager instance;

    // Key-value pairs loaded from the .properties file
    private final Properties properties;

    // Resolved environment name (e.g., "qa", "dev", "prod")
    private final String environment;

    /**
     * Private constructor — called only once via getInstance().
     * Resolves which environment to use, then loads its .properties file.
     */
    private ConfigManager() {
        this.environment = resolveEnvironment();
        this.properties = new Properties();
        loadProperties();
    }

    /**
     * Returns the singleton instance, creating it on first call.
     * Synchronized to prevent race conditions in multi-threaded test execution.
     *
     * WHY synchronized: If two test threads call getInstance() at the same time
     *   and instance is null, both could create separate instances — synchronized prevents that.
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Destroys the singleton — allows switching environments between test suites.
     * BENEFIT: In integration tests you can call reset() then set -Denv=staging
     *   to re-initialize with a different properties file mid-execution.
     */
    public static synchronized void reset() {
        instance = null;
    }

    /**
     * Retrieves a config property by key.
     * System properties (-D flags) take priority over .properties file values.
     * Returns null if the key is not found anywhere.
     *
     * WHY system-property-first: Allows CI/CD to override any config key without
     *   editing files. e.g., mvn test -Dbase.url=http://localhost:8080
     */
    public String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null) {
            return systemProp;
        }
        return properties.getProperty(key);
    }

    /** Same as getProperty(key) but returns defaultValue if key is not found. */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /** Returns base.url from config — e.g., "https://jsonplaceholder.typicode.com". */
    public String getBaseUrl() {
        return getProperty("base.url");
    }

    /** Returns auth.token from config — used by ApiClient.withAuth() for Bearer token. */
    public String getAuthToken() {
        return getProperty("auth.token");
    }

    /** Returns timeout.connection in ms (default: 10000). */
    public int getConnectionTimeout() {
        return Integer.parseInt(getProperty("timeout.connection", "10000"));
    }

    /** Returns timeout.read in ms (default: 10000). */
    public int getReadTimeout() {
        return Integer.parseInt(getProperty("timeout.read", "10000"));
    }

    /** Whether to log full request details to console (default: true). */
    public boolean shouldLogRequest() {
        return Boolean.parseBoolean(getProperty("log.request", "true"));
    }

    /** Whether to log full response details to console (default: true). */
    public boolean shouldLogResponse() {
        return Boolean.parseBoolean(getProperty("log.response", "true"));
    }

    /** Returns the resolved environment name (e.g., "qa"). */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Determines which environment to use via 3-step fallback:
     *   1. -Denv system property  (e.g., mvn test -Denv=dev)
     *   2. ENV OS environment variable  (e.g., set in Jenkins/GitHub Actions)
     *   3. Defaults to "qa" if neither is set
     * Result is lowercased for consistency.
     *
     * WHY 3-step: Flexibility — developers use -Denv locally, CI uses ENV variable,
     *   and "qa" is a safe default when nothing is specified.
     *
     * Example: mvn test -Denv=prod  → resolves to "prod" → loads config/prod.properties
     */
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

    /**
     * Loads config/{env}.properties from the classpath.
     * Throws RuntimeException if the file is not found — fails fast on misconfiguration.
     *
     * WHY fail-fast: Better to crash immediately with a clear message than run tests
     *   against the wrong URL and get confusing 404/connection errors later.
     */
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
