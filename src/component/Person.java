package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Person {
    private String myRequestedSysOpt;

    public Person() {}

    public void enterSystemOption() {
        Scanner scanner = new Scanner(System.in);
        this.myRequestedSysOpt = scanner.nextLine();
    }

    public void viewMenu(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap) {
        String border = "-".repeat(101), menu = border.concat("\n"), tmpMenu = "";
        String menuEntryStarterFormat = "|".concat(" ".repeat(3));

        for (Map.Entry<String, ArrayList<ArrayList<Food>>> entry: foodHashMap.entrySet()) {
            for (var element: entry.getValue()) {
                String menuEntry;

                String foodType = calculateExtraSpaceToAdd(
                        String.valueOf(entry.getKey()), 20, 25);
                String foodName = calculateExtraSpaceToAdd(
                        element.get(0).getFoodName(), 30, 35);
                String price = "$".concat(calculateExtraSpaceToAdd(
                        String.format("%.2f", element.get(0).getPrice()), 8, 10));

                int remainQty = element.get(0).getRemainQty(), maxQty = element.get(0).getMaxQty();
                String qty = String.valueOf(remainQty).concat(" / ").concat(String.valueOf(maxQty));
                qty = calculateExtraSpaceToAdd(qty, 10, 13);

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

    private String calculateExtraSpaceToAdd(String value, int maxLenOfAllValues, int totalLen) {
        String refomattedValue = null;

        if (value.length() < maxLenOfAllValues) {
            refomattedValue = value.concat(" ".repeat(totalLen - value.length()));
        }
        return refomattedValue;
    }

    public String getMyRequestedSysOpt() {
        return myRequestedSysOpt;
    }
}
