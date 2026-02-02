package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;

import static acetoys.session.UserSession.isCustomerLoggedIn;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.doIf;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.substring;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Cart {

    public static ChainBuilder viewCart =
            doIf(session -> !isCustomerLoggedIn(session))
                    .then(exec(Customer.login))
                    .exec(
                            http("View Cart")
                                    .get("/cart/view")
                                    .check(css("#CategoryHeader").is("Cart Overview"))
                    );

    public static ChainBuilder increaseQuantityInCart =
            exec(
                    http("Increase Product Quantity in Cart - Product Id: 19")
                            .get("/cart/add/19?cartPage=true")
            );

    public static ChainBuilder decreaseQuantityInCart =
            exec(
                    http("Subtract Product Quantity in Cart - Product Id: 19")
                            .get("/cart/subtract/19")
            );

    public static ChainBuilder checkout =
            exec(
                    http("Checkout")
                            .get("/cart/checkout")
                            .check(substring("Your products are on their way to you now!!"))
            );
}
