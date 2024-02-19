package view;

/**
 * This class compiles and formats food item options.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemView {
    public ItemView() {}

    // Compile and format available food item options
    //
    // This function compiles and styles food item options
    //
    // @param appState = the current operation that user wants to perform, menuHashMap = the latest list of food items
    //
    // @return The entire message that asks user to select a food item
    //
    public String printItemView(int appState, Map<String, HashMap<String, List<Food>>> menuHashMap) {
        List<String> itemList;
        String itemSelectMessage;
        String styledItemName;
        int itemNumber = 1;

        if (appState == 1) {
            itemList = new ArrayList<>(menuHashMap.keySet());
            itemSelectMessage = "What kind of item do you want to update?\n";
        } else {
            itemList = new ArrayList<>(menuHashMap.keySet().stream().limit(6).collect(Collectors.toList()));
            itemSelectMessage = "What would you like to have?\n";
        }

        for (var item : itemList) {
            // style item name
            if (!item.equalsIgnoreCase("drinks")) {
                styledItemName = "[\u001B[1m" + item.charAt(0) + "\u001B[0m]" + item.substring(1);
            } else {
                styledItemName = "[\u001B[1m" + item.substring(0,2) + "\u001B[0m]" + item.substring(2);
            }

            itemSelectMessage += itemNumber + ". " + styledItemName + "\n";
            itemNumber++;
        }
        itemSelectMessage += itemNumber + ". " + "<<[\u001B[1m[Ba\u001B[0m]ck to Previous>>\n";
        return itemSelectMessage;
    }
}
