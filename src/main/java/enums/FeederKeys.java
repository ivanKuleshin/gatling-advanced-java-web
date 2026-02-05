package enums;

public enum FeederKeys {

    // CSV
    CATEGORY_NAME("categoryName"),
    CATEGORY_SLUG("categorySlug"),

    // JSON
    ITEM_PRICE("price");

    private final String key;

    FeederKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
