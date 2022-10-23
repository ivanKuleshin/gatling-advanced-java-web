package acetoys.session;

import enums.SessionKeys;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.flushCookieJar;

public final class UserSession {

    private UserSession() {
        throw new UnsupportedOperationException("Utility class");
    }

    // default session initialization
    public static ChainBuilder initSession =
            exec(flushCookieJar())
                    .exec(session -> session.set(SessionKeys.CUSTOMER_LOGGED_IN.getKey(), false));

    public static ChainBuilder increaseItemsInBasketForSession =
            exec(session -> {
                int itemsInBasket = session.getInt("itemsInBasket");
                return session.set("itemsInBasket", (itemsInBasket + 1));
            });
//    .exec(session -> session.set("itemsInBasket", 0));

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
                    .exec(session -> session.set("customerLoggedIn", false));
}
