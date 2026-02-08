package acetoys.actions;

import acetoys.session.UserSession;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.Session;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static acetoys.session.UserSession.buildSessionKey;
import static enums.FeederKeys.CATEGORY_NAME;
import static enums.FeederKeys.CATEGORY_SLUG;
import static enums.SessionKeys.CATEGORY_PAGES;
import static enums.SessionKeys.CURRENT_PAGE_NUMBER;
import static enums.SessionKeys.EXPECTED_PAGE_NUMBER;
import static enums.SessionKeys.MORE_PAGES;
import static io.gatling.javaapi.core.CoreDsl.css;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CategoryActions {

    private static final FeederBuilder<String> categoryFeeder =
            csv("data/categoryDetails.csv").circular();

    private static final String HTTP_MESSAGE = "Load page #{%s} of Products - Category: #{%s}"
            .formatted(CURRENT_PAGE_NUMBER.getKey(), CATEGORY_NAME.getKey());
    private static final String URL = "/category/#{%s}?page=#{%s}"
            .formatted(CATEGORY_SLUG.getKey(), CURRENT_PAGE_NUMBER.getKey());

    private CategoryActions() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ChainBuilder openProductListByCategoryAction =
            feed(categoryFeeder)
                    .exec(
                            http("Load Products List Page - Category: " + buildSessionKey(CATEGORY_NAME.getKey()))
                                    .get("/category/" + buildSessionKey(CATEGORY_SLUG.getKey()))
                                    .check(css("#CategoryName").isEL(buildSessionKey(CATEGORY_NAME.getKey())))
                                    .check(css("ul.pagination > li").count().saveAs(CATEGORY_PAGES.getKey())));

    public static ChainBuilder cyclePagesOfProductsAction =
            exec(session -> {
                int currentPageNumber = BigDecimal.ZERO.intValue();
                boolean morePages = hasMorePagesAction(session, currentPageNumber);

                return setPageInfoAction(session, currentPageNumber, morePages);
            }).asLongAs(buildSessionKey(MORE_PAGES.getKey())).on(
                    // page index in url starts with 0, while displayed page number starts with 1
                    exec(session -> session.set(EXPECTED_PAGE_NUMBER.getKey(), getCurrentPageNumberAction(session) + 1))
                            .exec(http(HTTP_MESSAGE)
                                    .get(URL)
                                    .check(css(".page-item.active").isEL(buildSessionKey(EXPECTED_PAGE_NUMBER.getKey())))
                            )
                            .exec(updatePageInfoAction()));

    private static ChainBuilder updatePageInfoAction() {
        return exec(session -> {
            int currentPageNumber = session.getInt(CURRENT_PAGE_NUMBER.getKey()) + 1;
            boolean morePages = hasMorePagesAction(session, currentPageNumber);
            return setPageInfoAction(session, currentPageNumber, morePages);
        });
    }

    private static boolean hasMorePagesAction(Session session, int currentPageNumber) {
        int totalPages = getCategoryPagesCountAction(session);
        return currentPageNumber < totalPages;
    }

    private static int getCategoryPagesCountAction(Session session) {
        Integer categoryPages = UserSession.getSessionValue(session, CATEGORY_PAGES.getKey());

        // subtract 1 to exclude the "Next" button, default value is 0
        return Optional.ofNullable(categoryPages)
                .map(x -> x - BigDecimal.ONE.intValue())
                .orElse(BigDecimal.ZERO.intValue());
    }

    private static int getCurrentPageNumberAction(Session session) {
        Integer currentPagerNumber = UserSession.getSessionValue(session, CURRENT_PAGE_NUMBER.getKey());

        return Optional.ofNullable(currentPagerNumber)
                .orElseThrow(() -> new RuntimeException("'%s' session key not found!".formatted(CURRENT_PAGE_NUMBER.getKey())));
    }

    private static Session setPageInfoAction(Session session, int currentPageNumber, boolean morePages) {
        return session.setAll(Map.of(
                CURRENT_PAGE_NUMBER.getKey(), currentPageNumber,
                MORE_PAGES.getKey(), morePages));
    }
}
