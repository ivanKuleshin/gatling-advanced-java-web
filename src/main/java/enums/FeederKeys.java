package enums;

public enum FeederKeys {

    CATEGORY_NAME("categoryName"),
    CATEGORY_SLUG("categorySlug");

    private final String key;

    FeederKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
