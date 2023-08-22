package sample;

import java.sql.Date;

public class customerData {
    private Integer id;
    private Integer customerId;
    private String  itemName;
    private String  itemBarcode;
    private double  itemPrice;
    private String    date;

    public customerData(Integer id,Integer customerId, String itemName, String itemBarcode, double itemPrice, String date) {
        this.id=id;
        this.customerId = customerId;
        this.itemName = itemName;
        this.itemBarcode = itemBarcode;
        this.itemPrice = itemPrice;
        this.date = date;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
