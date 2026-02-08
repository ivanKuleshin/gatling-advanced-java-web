package acetoys.populations;

import acetoys.scenarios.TestScenarios;
import acetoys.utils.PropertyUtils;
import io.gatling.javaapi.core.PopulationBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;

public class OpenPopulations {

    private static final int USER_COUNT = PropertyUtils.getIntProperty("USER_COUNT", 10);
    private static final Duration RAMP_DURATION = Duration.ofSeconds(PropertyUtils.getIntProperty("RAMP_DURATION", 20));

    public static PopulationBuilder instantUsersPopulation =
            TestScenarios.defaultLoadScenario.injectOpen(
                    nothingFor(Duration.ofSeconds(5)),
                    atOnceUsers(USER_COUNT)
            );

    public static PopulationBuilder rampUsersPopulation =
            TestScenarios.highPurchaseLoadScenario.injectOpen(
                    nothingFor(Duration.ofSeconds(5)),
                    rampUsers(USER_COUNT).during(RAMP_DURATION)
            );

    public static PopulationBuilder usersPerSecondPopulation =
            TestScenarios.defaultLoadScenario.injectOpen(
                    nothingFor(Duration.ofSeconds(5)),
                    constantUsersPerSec(USER_COUNT).during(Duration.ofSeconds(20)).randomized(),
                    rampUsersPerSec(USER_COUNT).to(USER_COUNT * 2).during(RAMP_DURATION).randomized()
            );
}
