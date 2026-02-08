package acetoys.scenarios;

import io.gatling.javaapi.core.PopulationBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers;
import static io.gatling.javaapi.core.CoreDsl.rampConcurrentUsers;

public class ClosedPopulations {

    public static PopulationBuilder constantUsersPopulation =
            TestScenarios.highPurchaseLoadScenario.injectClosed(
                    constantConcurrentUsers(10).during(Duration.ofSeconds(20)),
                    rampConcurrentUsers(10).to(20).during(Duration.ofSeconds(20))

            );
}
