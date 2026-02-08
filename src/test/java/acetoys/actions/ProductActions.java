package acetoys.actions;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ProductActions {

    private static final FeederBuilder<Object> productFeeder =
            jsonFile("data/productDetails.json").random();

    public static ChainBuilder loadProductDetailsPageAction =
            feed(productFeeder)
                    .exec(
                            http("Load Products Details Page - Product: #{name}")
                                    .get("/product/#{slug}")
                                    .check(css("#ProductDescription").isEL("#{description}"))
                    );
}
