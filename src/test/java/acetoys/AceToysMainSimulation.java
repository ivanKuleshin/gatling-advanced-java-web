package acetoys;

import acetoys.populations.OpenPopulations;
import acetoys.scenarios.ClosedPopulations;
import annotation.GatlingSimulation;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.http.HttpDsl.http;

@GatlingSimulation
public class AceToysMainSimulation extends Simulation {

    private static final String DOMAIN = System.getProperty("DOMAIN", "acetoys.uk");
    private static final String TEST_TYPE = System.getProperty("TEST_TYPE", "INSTANT_USERS");

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://" + DOMAIN)
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-GB,en;q=0.9");

    /**
     * <h3>Constructs an AceToysMainSimulation and sets up the load test.</h3>
     * <p>
     * The test type can be controlled via the "TEST_TYPE" system property.
     * <p>Supported values:</p>
     * <ul>
     * <li>"INSTANT_USERS": Injects all users at once after an initial delay</li>
     * <li>"RAMP_USERS": Gradually ramps up users over a specified duration</li>
     * <li>"COMPLEX_SCENARIO": Starts with constant users per second, then ramps up the rate</li>
     * <li>"CLOSED_MODEL": Uses a closed model with constant and ramping concurrent users</li>
     * </ul>
     * Default is "INSTANT_USERS".
     * <p>
     * <h3>Additional system properties:</h3>
     * <ul>
     * <li>"DOMAIN": Target domain for testing (default: "acetoys.uk")</li>
     * <li>"USER_COUNT": Number of users (default: 10, must be positive)</li>
     * <li>"RAMP_DURATION": Ramp duration in seconds (default: 20, must be positive)</li>
     * <li>"TEST_DURATION": Test duration in seconds (default: 30, must be positive)</li>
     * </ul>
     * <p>
     * For better readability, all scenarios are defined in TestScenarios and populations in OpenPopulations/ClosedPopulations
     * 
     * @throws IllegalArgumentException if any numeric property is invalid
     * @throws IllegalStateException if required resource files are missing
     */
    public AceToysMainSimulation() {
        // Validate numeric properties
        validatePositiveInteger("USER_COUNT");
        validatePositiveInteger("RAMP_DURATION");
        validatePositiveInteger("TEST_DURATION");
        
        // Validate required resource files
        validateResourceFile("data/categoryDetails.csv");
        validateResourceFile("data/productDetails.json");
        
        switch (TEST_TYPE) {
            case "INSTANT_USERS" -> setUp(OpenPopulations.instantUsersPopulation).protocols(httpProtocol);
            case "RAMP_USERS" -> setUp(OpenPopulations.rampUsersPopulation).protocols(httpProtocol);
            case "COMPLEX_SCENARIO" -> setUp(OpenPopulations.usersPerSecondPopulation).protocols(httpProtocol);
            case "CLOSED_MODEL" -> setUp(ClosedPopulations.constantUsersPopulation).protocols(httpProtocol);
            default -> {
                System.err.println("WARNING: Invalid TEST_TYPE '" + TEST_TYPE + "'. Defaulting to INSTANT_USERS.");
                System.err.println("Valid values: INSTANT_USERS, RAMP_USERS, COMPLEX_SCENARIO, CLOSED_MODEL");
                setUp(OpenPopulations.instantUsersPopulation).protocols(httpProtocol);
            }
        }
    }
    
    /**
     * Validates that a system property is a positive integer.
     * 
     * @param propertyName the name of the system property to validate
     * @throws IllegalArgumentException if the property value is not a positive integer
     */
    private void validatePositiveInteger(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue <= 0) {
                    throw new IllegalArgumentException(
                        propertyName + " must be a positive integer, got: " + intValue
                    );
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    propertyName + " must be a valid integer, got: " + value, e
                );
            }
        }
    }
    
    /**
     * Validates that a required resource file exists.
     * First checks the classpath (as Gatling will access it), then checks the file system
     * to provide a more detailed error message if the resource is not found.
     * 
     * @param resourcePath the path to the resource file (relative to resources directory)
     * @throws IllegalStateException if the resource file does not exist
     */
    private void validateResourceFile(String resourcePath) {
        // Try to load as classpath resource first (this is how Gatling will access it)
        if (getClass().getClassLoader().getResource(resourcePath) == null) {
            // Also check in the file system for better error messages
            Path filePath = Paths.get("src/test/resources", resourcePath);
            if (!Files.exists(filePath)) {
                throw new IllegalStateException(
                    "Required resource file not found: " + resourcePath + 
                    ". Expected at: src/test/resources/" + resourcePath
                );
            }
        }
    }
}
