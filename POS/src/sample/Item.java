package sample;

public class Item {
    private String itemId;
    private String itemName;
    private String itemBarcode;
    private double itemPrice;

    public Item(String itemId, String itemName, String itemBarcode, double itemPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemBarcode = itemBarcode;
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
