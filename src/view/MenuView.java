package view;

import component.Food;
import general.General;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuView {
    public MenuView() {}

    public void printMenuView(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap) {
        String border = "-".repeat(101), menu = border.concat("\n"), tmpMenu = "";
        String menuEntryStarterFormat = "|".concat(" ".repeat(3));

        for (Map.Entry<String, ArrayList<ArrayList<Food>>> entry: foodHashMap.entrySet()) {
            for (var element: entry.getValue()) {
                String menuEntry;

                String foodType = General.calculateExtraSpaceToAdd(
                        String.valueOf(entry.getKey()), 20, 25);
                String foodName = General.calculateExtraSpaceToAdd(
                        element.get(0).getFoodName(), 30, 35);
                String price = "$".concat(General.calculateExtraSpaceToAdd(
                        String.format("%.2f", element.get(0).getPrice()), 8, 10));

                int remainQty = element.get(0).getRemainQty(), maxQty = element.get(0).getMaxQty();
                String qty = String.valueOf(remainQty).concat(" / ").concat(String.valueOf(maxQty));
                qty = General.calculateExtraSpaceToAdd(qty, 10, 13);

                menuEntry = menuEntryStarterFormat
                        .concat(foodType)
                        .concat("|   ".concat(foodName))
                        .concat(menuEntryStarterFormat).concat(price)
                        .concat("|   ".concat(qty.concat("|")))
                        .concat("\n");

                tmpMenu += menuEntry;
            }
        }

        menu += tmpMenu.concat(border);

        System.out.println(menu);
    }
}
