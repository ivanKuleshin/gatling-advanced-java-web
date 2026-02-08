package acetoys.scenarios;

import acetoys.actions.MainActions;
import acetoys.utils.PropertyUtils;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.percent;
import static io.gatling.javaapi.core.CoreDsl.randomSwitch;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class TestScenarios {

    private static final Duration TEST_DURATION = Duration.ofSeconds(PropertyUtils.getIntProperty("TEST_DURATION", 30));

    public final static ScenarioBuilder defaultLoadScenario =
            scenario("Default Load Scenario")
                    .during(TEST_DURATION)
                    .on(randomSwitch().on(
                            percent(60).then(CoreDsl.exec(MainActions.browserTheStoreActions)),
                            percent(30).then(exec(MainActions.abandonCartActions)),
                            percent(10).then(exec(MainActions.completePurchaseActions))
                    ));

    public final static ScenarioBuilder highPurchaseLoadScenario =
            scenario("High Purchase Load Scenario")
                    .during(TEST_DURATION)
                    .on(randomSwitch().on(
                            percent(30).then(exec(MainActions.browserTheStoreActions)),
                            percent(30).then(exec(MainActions.abandonCartActions)),
                            percent(40).then(exec(MainActions.completePurchaseActions))
                    ));
}
