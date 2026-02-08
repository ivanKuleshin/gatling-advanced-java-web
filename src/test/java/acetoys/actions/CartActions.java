package acetoys.actions;

import io.gatling.javaapi.core.ChainBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static acetoys.session.UserSession.buildSessionKey;
import static acetoys.session.UserSession.isCustomerLoggedIn;
import static enums.FeederKeys.ITEM_PRICE;
import static enums.SessionKeys.CART_TOTAL_PRICE;
import static enums.SessionKeys.ITEMS_COUNT_IN_CART;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.doIf;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.substring;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CartActions {

    public static ChainBuilder viewCartAction =
            doIf(session -> !isCustomerLoggedIn(session))
                    .then(exec(CustomerActions.loginAction))
                    .exec(
                            http("View Cart")
                                    .get("/cart/view")
                                    .check(css("#CategoryHeader").is("Cart Overview"))
                    );

    public static ChainBuilder increaseQuantityInCartAction =
            exec(increaseItemsInCartForSessionAction())
                    .exec(increaseCartTotalForSessionAction())
                    .exec(
                            http("Increase Product Quantity in Cart - Product Name: #{name}")
                                    .get("/cart/add/#{id}?cartPage=true")
                                    .check(css("#grandTotal").isEL("$" + buildSessionKey(CART_TOTAL_PRICE.getKey())))
                    );

    public static ChainBuilder decreaseQuantityInCartAction =
            exec(CartActions::decreaseItemsInCartForSessionAction).exec(CartActions::decreaseCartTotalForSessionAction)
                    .exec(
                            http("Subtract Product Quantity in Cart - Product Id: 19")
                                    .get("/cart/subtract/#{id}")
                                    .check(css("#grandTotal").isEL("$" + buildSessionKey(CART_TOTAL_PRICE.getKey())))
                    );


    public static ChainBuilder checkoutAction =
            exec(
                    http("Checkout")
                            .get("/cart/checkout")
                            .check(substring("Your products are on their way to you now!!"))
            );

    public static final ChainBuilder addProductToCartAction =
            exec(CartActions::increaseItemsInCartForSessionAction)
                    .exec(
                            http("Add Product to Cart - Product Name: #{name}")
                                    .get("/cart/add/#{id}")
                                    .check(substring("You have <span>" + buildSessionKey(ITEMS_COUNT_IN_CART.getKey()) + "</span> products in your Basket"))
                    )
                    .exec(CartActions::increaseCartTotalForSessionAction);

    private static ChainBuilder increaseItemsInCartForSessionAction() {
        return exec(session -> {
            int itemsInCart = session.getInt(ITEMS_COUNT_IN_CART.getKey()) + 1;
            return session.set(ITEMS_COUNT_IN_CART.getKey(), itemsInCart);
        });
    }

    private static ChainBuilder decreaseItemsInCartForSessionAction() {
        return exec(session -> {
            int itemsInCart = session.getInt(ITEMS_COUNT_IN_CART.getKey()) - 1;
            return session.set(ITEMS_COUNT_IN_CART.getKey(), itemsInCart);
        });
    }

    private static ChainBuilder increaseCartTotalForSessionAction() {
        return exec(session -> {
            double cartTotal = session.getDouble(CART_TOTAL_PRICE.getKey());

            double itemPrice = session.getDouble(ITEM_PRICE.getKey());
            BigDecimal newCartTotal = new BigDecimal(cartTotal).add(new BigDecimal(itemPrice))
                    .setScale(2, RoundingMode.HALF_EVEN);

            return session.set(CART_TOTAL_PRICE.getKey(), newCartTotal.doubleValue());
        });
    }

    private static ChainBuilder decreaseCartTotalForSessionAction() {
        return exec(session -> {
            double cartTotal = session.getDouble(CART_TOTAL_PRICE.getKey());

            double itemPrice = session.getDouble(ITEM_PRICE.getKey());
            BigDecimal newCartTotal = new BigDecimal(cartTotal).subtract(new BigDecimal(itemPrice))
                    .setScale(2, RoundingMode.HALF_EVEN);

            return session.set(CART_TOTAL_PRICE.getKey(), newCartTotal.doubleValue());
        });
    }
}
