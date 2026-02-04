package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static acetoys.session.UserSession.setCustomerLoggedIn;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.pause;
import static io.gatling.javaapi.core.CoreDsl.percent;
import static io.gatling.javaapi.core.CoreDsl.randomSwitch;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Customer {

    private static final Iterator<Map<String, Object>> loginFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();
                int userId = rand.nextInt(3 - 1 + 1) + 1;

                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("userId", "user" + userId);
                hmap.put("password", "pass");
                return hmap;
            }).iterator();

    public static ChainBuilder login =
            feed(loginFeeder)
                    .exec(
                            http("Login User")
                                    .post("/login")
                                    .formParam("_csrf", "#{csrfToken}")
                                    .formParam("username", "#{userId}")
                                    .formParam("password", "#{password}")
                                    .check(css("#_csrf", "content").saveAs("csrfTokenLoggedIn"))
                    )
                    .exec(session -> setCustomerLoggedIn(session, true));

    // new Gatling 3.11 and above
    public static ChainBuilder logout =
            randomSwitch().on(
                    percent(10).then(exec(
                            http("Logout")
                                    .post("/logout")
                                    .formParam("_csrf", "#{csrfTokenLoggedIn}")
                                    .check(css("ul.float-right.navbar-nav  a#NavbarHeaderLink").is("Login"))
                    )),
                    percent(90).then(exec(pause(0)))
            );
}
