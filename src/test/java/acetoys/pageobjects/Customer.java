package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.pause;
import static io.gatling.javaapi.core.CoreDsl.percent;
import static io.gatling.javaapi.core.CoreDsl.randomSwitch;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Customer {

    public static ChainBuilder login =
            exec(
                    http("Login User")
                            .post("/login")
                            .formParam("_csrf", "#{csrfToken}")
                            .formParam("username", "user1")
                            .formParam("password", "pass")
                            .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn"))
            );

    // new Gatling 3.11 and above
    public static ChainBuilder logout =
            randomSwitch().on(
                    percent(10).then(exec(
                            http("Logout")
                                    .post("/logout")
                                    .formParam("_csrf", "#{csrfTokenLoggedIn}")
                                    .check(css("#LoginLink").is("Login"))
                    )),
                    percent(90).then(exec(pause(0)))
            );
}
