package Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by savinda on 11/4/16.
 */

public class OrderItem implements Serializable {
    String ItemId;
    String ItemQty;
    String ItemName;
    String OrderItemId;

    public OrderItem(String itemId, String itemQty, String orderItemId) {
        ItemId = itemId;
        ItemQty = itemQty;
        OrderItemId = orderItemId;
    }

    public String getOrderItemId() {
        return OrderItemId;
    }

    public String getItemId() {
        return ItemId;
    }

    public String getItemQty() {
        return ItemQty;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(ArrayList<MenuItems> menu) {
        //ArrayList<MenuItems> menu1 = menu;
        int z = menu.size();
        for(int i = 0; i<z; i++){
            MenuItems item = menu.get(i);
            if(item.getItemCode()==ItemId){
                ItemName=item.getItemName();
                break;
            }
        }
        ItemName="test";
    }
}