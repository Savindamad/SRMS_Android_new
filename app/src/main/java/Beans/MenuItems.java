package Beans;

import java.io.Serializable;

/**
 * Created by savinda on 11/4/16.
 */

public class MenuItems implements Serializable {
    public String itemCode;
    public String itemName;
    public String itemType;
    public String itemDescription;
    public String itemPrice;
    public int itemQty = 1;

    public MenuItems(String itemCode,String itemName, String itemType, String itemDescription,String itemPrice){
        this.itemCode=itemCode;
        this.itemName=itemName;
        this.itemType=itemType;
        this.itemDescription=itemDescription;
        this.itemPrice=itemPrice;

    }
    public int getItemQty(){
        return itemQty;
    }
    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
    public void setItemQty(int itemQty){
        this.itemQty=itemQty;
    }
}
