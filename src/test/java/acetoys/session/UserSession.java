package acetoys.session;

import enums.SessionKeys;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import static enums.SessionKeys.CART_TOTAL_PRICE;
import static enums.SessionKeys.ITEMS_COUNT_IN_CART;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.flushCookieJar;

public final class UserSession {

    private UserSession() {
        throw new UnsupportedOperationException("Utility class");
    }

    // default session initialization
    public static ChainBuilder initSession =
            exec(flushCookieJar())
                    .exec(session -> session.set(SessionKeys.CUSTOMER_LOGGED_IN.getKey(), false))
                    .exec(session -> session.set(ITEMS_COUNT_IN_CART.getKey(), 0))
                    .exec(session -> session.set(CART_TOTAL_PRICE.getKey(), 0.00));

    public static boolean isCustomerLoggedIn(Session session) {
        validateSessionAndKey(session, SessionKeys.CUSTOMER_LOGGED_IN.getKey());
        return session.getBoolean(SessionKeys.CUSTOMER_LOGGED_IN.getKey());
    }

    public static Session setCustomerLoggedIn(Session session, boolean loggedIn) {
        return session.set(SessionKeys.CUSTOMER_LOGGED_IN.getKey(), loggedIn);
    }

    @SuppressWarnings("unused")
    public static <T> T getSessionValue(Session session, String key) {
        validateSessionAndKey(session, key);
        return session.get(key);
    }

    public static String buildSessionKey(String key) {
        return "#{" + key + "}";
    }

    private static void validateSessionAndKey(Session session, String key) {
        validateSession(session);
        validateKey(session, key);
    }

    private static void validateSession(Session session) {
        if (session == null) {
            throw new RuntimeException("Session is null");
        }
    }

    private static void validateKey(Session session, String key) {
        if (!session.contains(key)) {
            throw new RuntimeException("Session does not contain '" + key + "' key");
        }
    }
}
