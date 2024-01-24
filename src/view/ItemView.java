package view;

import component.Food;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemView {
    public ItemView() {}

    public String printItemView(HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap) {
        ArrayList<String> itemList = new ArrayList<>(menuHashMap.keySet());

        String itemSelectMessage = "What kind of item do you want to update?\n";
        String styledItemName;
        int itemNumber = 1;

        for (var item: itemList) {
            // style item name
            if (!item.equalsIgnoreCase("drinks")) {
                styledItemName = "[\u001B[1m" + item.substring(0,1) + "\u001B[0m]" + item.substring(1);
            }
            else {
                styledItemName = "[\u001B[1m" + item.substring(0,2) + "\u001B[0m]" + item.substring(2);
            }

            itemSelectMessage += itemNumber + ". " + styledItemName + "\n";
            itemNumber++;
        }
        itemSelectMessage += itemNumber + ". " + "<<[\u001B[1m[Ba\u001B[0m]ck to Previous>>\n";
        return itemSelectMessage;
    }
}
