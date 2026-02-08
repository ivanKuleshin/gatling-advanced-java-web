package acetoys.actions;

import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;

import static acetoys.session.UserSession.initSession;
import static io.gatling.javaapi.core.CoreDsl.exec;

public class MainActions {

    public static final Duration LOW_PAUSE_TIME = Duration.ofMillis(1000);
    public static final Duration HIGH_PAUSE_TIME = Duration.ofMillis(3000);

    public static ChainBuilder browserTheStoreActions =
            exec(initSession)
                    .exec(StaticPageActions.openHomePageAction)
                    .pause(HIGH_PAUSE_TIME)
                    .exec(StaticPageActions.openOurStoryPageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(StaticPageActions.openGetInTouchPageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .repeat(3).on(
                            exec(CategoryActions.openProductListByCategoryAction)
                                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                                    .exec(CategoryActions.cyclePagesOfProductsAction)
                                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                                    .exec(ProductActions.loadProductDetailsPageAction)
                    );

    public static ChainBuilder abandonCartActions =
            exec(initSession)
                    .exec(StaticPageActions.openHomePageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CategoryActions.openProductListByCategoryAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(ProductActions.loadProductDetailsPageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CartActions.addProductToCartAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME);

    public static ChainBuilder completePurchaseActions =
            exec(initSession)
                    .exec(StaticPageActions.openHomePageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CategoryActions.openProductListByCategoryAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(ProductActions.loadProductDetailsPageAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CartActions.addProductToCartAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CartActions.viewCartAction)
                    .pause(LOW_PAUSE_TIME)
                    .exec(CartActions.increaseQuantityInCartAction)
                    .pause(HIGH_PAUSE_TIME)
                    .exec(CartActions.checkoutAction)
                    .pause(LOW_PAUSE_TIME, HIGH_PAUSE_TIME)
                    .exec(CustomerActions.logoutWithProbabilityAction);

}
