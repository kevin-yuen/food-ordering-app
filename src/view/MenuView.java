package view;

import component.Food;
import general.General;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuView {
    public MenuView() {}

    public void printMenuView(Map<String, HashMap<String, List<Food>>> foodHashMap) {
        String border = "-".repeat(101), menu = border.concat("\n"), tmpMenu = "";
        String menuEntryStarterFormat = "|".concat(" ".repeat(3));

        for (Map.Entry<String, HashMap<String, List<Food>>> entry : foodHashMap.entrySet()) {
            for (Map.Entry<String, List<Food>> e : entry.getValue().entrySet()) {
                String menuEntry;
                Food foodDets = e.getValue().get(0);

                String itemName = General.calculateExtraSpaceToAdd(
                            String.valueOf(entry.getKey()), 20, 25),
                        foodName = General.calculateExtraSpaceToAdd(
                            e.getKey(), 30, 35),
                        price = "$".concat(General.calculateExtraSpaceToAdd(
                            String.format("%.2f", foodDets.getPrice()), 8, 10));

                int remainQty = foodDets.getRemainQty(), maxQty = foodDets.getMaxQty();
                String qty = String.valueOf(remainQty).concat(" / ").concat(String.valueOf(maxQty));
                qty = General.calculateExtraSpaceToAdd(qty, 10, 13);

                menuEntry = menuEntryStarterFormat
                        .concat(itemName)
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
