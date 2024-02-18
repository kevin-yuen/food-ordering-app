package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToppingSelectionResultView {
    public ToppingSelectionResultView() {}

    public void determineToppingResultView(List<HashMap<String, Double>> toppingsOrder, List<String> invalidToppingsOrder) {

        if (invalidToppingsOrder.size() > 0 && toppingsOrder.size() == 0) {
            printAllInvalidToppingsView();
        }
        else if (invalidToppingsOrder.size() == 0 && toppingsOrder.size() > 0){
            printAllValidToppingsView();
        }
        else if (invalidToppingsOrder.size() > 0) {     // and toppingsOrder.size() > 0
            printSomeInvalidToppingsView(toppingsOrder);
        }
        else {  // customer did not enter any input for topping selection
            System.out.println("Please enter at least one topping");
        }
    }

    private void printSomeInvalidToppingsView(List<HashMap<String, Double>> toppingsOrder) {
        String validToppings = "";
        for (var tElement: toppingsOrder) {
            for (Map.Entry<String, Double> tMapElement: tElement.entrySet()) {
                String topping = tMapElement.getKey();

                if (toppingsOrder.size() > 2) {
                    if (toppingsOrder.indexOf(topping) < toppingsOrder.size()-2) {
                        topping = topping.concat(", ");
                    }
                    else if (toppingsOrder.indexOf(topping) == toppingsOrder.size()-2) {
                        topping = topping.concat(" and ");
                    }
                }
                else if (toppingsOrder.size() == 2) {
                    if (toppingsOrder.indexOf(topping) == 0) topping = topping.concat(" and ");
                }

                validToppings += topping;
            }
        }

        System.out.println(String.format("We're sorry. We've only added %s. We don't have some of those toppings you requested.", validToppings));
    }

    private void printAllInvalidToppingsView() {
        System.out.println("We're sorry. We don't have those toppings in stock.");
    };

    private void printAllValidToppingsView() {
        System.out.println("The toppings have been added.");
    }
}
