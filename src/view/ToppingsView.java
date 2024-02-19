package view;

import component.Food;
import service.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToppingsView {
    public ToppingsView() {}

    public String printToppingsView() {
        HashMap<String, List<Food>> toppings = Global.getMenuHashMap().get("Toppings");
        List<String> toppingNames = new ArrayList<>(toppings.keySet());
        String message = "Please enter toppings (separate each by comma):\n";
        int toppingCount = 1;

        for (var toppingName : toppingNames) {
            if (toppingName.equalsIgnoreCase("Grilled Onions") || toppingName.equalsIgnoreCase("Grilled Mushrooms")) {
                String[] tempToppingName = toppingName.split(" ");
                String fmtToppingName = "";

                for (var tName : tempToppingName) {
                    fmtToppingName += String.format("[\u001B[1m%s\u001B[0m]%s ", tName.charAt(0), tName.substring(1));
                }
                fmtToppingName = fmtToppingName.trim();
                message += String.format("%s. %s%s\n", toppingCount, fmtToppingName.charAt(0), fmtToppingName.substring(1));
            } else if (toppingName.equalsIgnoreCase("Mustard")) {
                message += String.format("%s. [\u001B[1m%s\u001B[0m]%s\n", toppingCount, toppingName.substring(0, 2), toppingName.substring(2));
            } else {
                message += String.format("%s. [\u001B[1m%s\u001B[0m]%s\n", toppingCount, toppingName.charAt(0), toppingName.substring(1));
            }
            toppingCount++;
        }
        message = message.trim();   // remove the last space
        return message;
    }
}
