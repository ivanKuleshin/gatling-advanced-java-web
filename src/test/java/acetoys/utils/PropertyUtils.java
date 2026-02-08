package acetoys.utils;

/**
 * Utility class for handling system properties.
 */
public class PropertyUtils {

    /**
     * Retrieves an integer system property with a default value and error handling.
     *
     * @param key          The system property key
     * @param defaultValue The default value to use if the property is not set or invalid
     * @return The integer value of the property, or the default value if parsing fails
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(System.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            System.err.println("Invalid value for " + key + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
}
