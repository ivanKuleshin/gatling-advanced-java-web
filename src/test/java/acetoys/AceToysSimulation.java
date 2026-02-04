package acetoys;

import acetoys.pageobjects.Cart;
import acetoys.pageobjects.Category;
import acetoys.pageobjects.Customer;
import acetoys.pageobjects.Product;
import acetoys.pageobjects.StaticPages;
import acetoys.session.UserSession;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

@SuppressWarnings("unused")
public class AceToysSimulation extends Simulation {

    private static final String DOMAIN = "acetoys.uk";

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://" + DOMAIN)
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-GB,en;q=0.9");

    private static final ScenarioBuilder scn = scenario("AceToysSimulation")
            .exec(UserSession.initSession)
            .exec(StaticPages.homepage)
            .pause(2)
            .exec(StaticPages.ourStory)
            .pause(2)
            .exec(StaticPages.getInTouch)
            .pause(2)
            .exec(Category.productListByCategory)
            .pause(2)
            .exec(Category.cyclePagesOfProducts)
            .pause(2)
            .exec(Product.loadProductDetailsPage)
            .pause(2)
            .exec(Cart.addProductToCart)
            .pause(2)
            .exec(Category.productListByCategory)
            .pause(2)
            .exec(Cart.addProductToCart)
            .pause(2)
            .exec(Cart.viewCart)
            .pause(2)
            .exec(Cart.increaseQuantityInCart)
            .pause(2)
            .exec(Cart.increaseQuantityInCart)
            .pause(2)
            .exec(Cart.decreaseQuantityInCart)
            .pause(2)
            .exec(Cart.viewCart)
            .pause(2)
            .exec(Cart.checkout)
            .pause(2)
            .exec(Customer.logout);

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
