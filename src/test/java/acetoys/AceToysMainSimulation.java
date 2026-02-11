package acetoys;

import acetoys.actions.CartActions;
import acetoys.actions.CategoryActions;
import acetoys.actions.CustomerActions;
import acetoys.actions.MainActions;
import acetoys.actions.ProductActions;
import acetoys.actions.StaticPageActions;
import acetoys.populations.OpenPopulations;
import acetoys.scenarios.ClosedPopulations;
import acetoys.scenarios.TestScenarios;
import acetoys.session.UserSession;
import annotation.GatlingSimulation;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

@GatlingSimulation
public class AceToysMainSimulation extends Simulation {

    private static final String DOMAIN = "acetoys.uk";
    private static final String TEST_TYPE = System.getProperty("TEST_TYPE", "INSTANT_USERS");

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://" + DOMAIN)
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-GB,en;q=0.9");

    private static final ScenarioBuilder defaultScenario = scenario("Default AceToys Simulation")
            .exec(UserSession.initSession)
            .exec(StaticPageActions.openHomePageAction)
            .pause(2)
            .exec(StaticPageActions.openOurStoryPageAction)
            .pause(2)
            .exec(StaticPageActions.openGetInTouchPageAction)
            .pause(2)
            // CSV is loaded
            .exec(CategoryActions.openProductListByCategoryAction)
            .pause(2)
            .exec(CategoryActions.cyclePagesOfProductsAction)
            .pause(2)
            // JSON is loaded
            .exec(ProductActions.loadProductDetailsPageAction)
            .pause(2)
            .exec(CartActions.addProductToCartAction)
            .pause(2)
            .exec(CategoryActions.openProductListByCategoryAction)
            .pause(2)
            .exec(CartActions.addProductToCartAction)
            .pause(2)
            .exec(CartActions.viewCartAction)
            .pause(2)
            .exec(CartActions.increaseQuantityInCartAction)
            .pause(2)
            .exec(CartActions.increaseQuantityInCartAction)
            .pause(2)
            .exec(CartActions.decreaseQuantityInCartAction)
            .pause(2)
            .exec(CartActions.viewCartAction)
            .pause(2)
            .exec(CartActions.checkoutAction)
            .pause(2)
            .exec(CustomerActions.logoutWithProbabilityAction);

    private static final ScenarioBuilder browserTheStoreScenario =
            scenario("AceToys Just Browsing The Store Scenario")
                    .exec(MainActions.browserTheStoreActions);

    private static final ScenarioBuilder abandonCartScenario =
            scenario("AceToys Abandon Cart Scenario")
                    .exec(MainActions.abandonCartActions);

    private static final ScenarioBuilder completePurchaseScenario =
            scenario("AceToys Complete Purchase Scenario")
                    .exec(MainActions.completePurchaseActions);

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
     * <li>"USER_COUNT": Number of users (default: 10)</li>
     * <li>"RAMP_DURATION": Ramp duration in seconds (default: 20)</li>
     * <li>"TEST_DURATION": Test duration in seconds (default: 30)</li>
     * </ul>
     * <p>
     * For better readability all scenarios are defined in {@link TestScenarios}
     */
    public AceToysMainSimulation() {
        switch (TEST_TYPE) {
            case "INSTANT_USERS" -> setUp(OpenPopulations.instantUsersPopulation).protocols(httpProtocol)
                    // less than 800ms
                    .assertions(global().responseTime().max().lt(800));
            case "RAMP_USERS" -> setUp(OpenPopulations.rampUsersPopulation).protocols(httpProtocol);
            case "COMPLEX_SCENARIO" -> setUp(OpenPopulations.usersPerSecondPopulation).protocols(httpProtocol);
            case "CLOSED_MODEL" -> setUp(ClosedPopulations.constantUsersPopulation).protocols(httpProtocol);
            default -> throw new RuntimeException("Invalid test type");
        }
    }
}
