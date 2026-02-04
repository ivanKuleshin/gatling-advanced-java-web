package enums;

public enum SessionKeys {

    CUSTOMER_LOGGED_IN("customerLoggedIn"),
    CATEGORY_PAGES("categoryPages"),
    CURRENT_PAGE_NUMBER("currentPageNumber"),
    EXPECTED_PAGE_NUMBER("expectedPageNumber"),
    MORE_PAGES("morePages");

    private final String key;

    SessionKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
