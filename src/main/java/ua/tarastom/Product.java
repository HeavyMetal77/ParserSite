package ua.tarastom;

public class Product {
    private String articul;
    private int productId;
    private String productUrl;
    private String productName;
    private String productPrice;
    private String productMinQuantity;
    private String productBigPicture;
    private String productSmallPicture;
    private String productPresenceData; //наявність
    private String category;

    public Product(String articul, int productId, String productUrl, String productName, String productPrice,
                   String productMinQuantity, String productBigPicture, String productSmallPicture,
                   String productPresenceData) {
        this.articul = articul;
        this.productId = productId;
        this.productUrl = productUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productMinQuantity = productMinQuantity;
        this.productBigPicture = productBigPicture;
        this.productSmallPicture = productSmallPicture;
        this.productPresenceData = productPresenceData;
        this.category = "";
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductMinQuantity() {
        return productMinQuantity;
    }

    public void setProductMinQuantity(String productMinQuantity) {
        this.productMinQuantity = productMinQuantity;
    }

    public String getProductBigPicture() {
        return productBigPicture;
    }

    public void setProductBigPicture(String productBigPicture) {
        this.productBigPicture = productBigPicture;
    }

    public String getProductSmallPicture() {
        return productSmallPicture;
    }

    public void setProductSmallPicture(String productSmallPicture) {
        this.productSmallPicture = productSmallPicture;
    }

    public String getProductPresenceData() {
        return productPresenceData;
    }

    public void setProductPresenceData(String productPresenceData) {
        this.productPresenceData = productPresenceData;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "\n" + articul +
                ", " + category +
                ", " + productName +
                ", " + productUrl +
                ", " + productPrice +
                ", min='" + productMinQuantity + '\'' +
                ", " + productPresenceData;
    }
}
