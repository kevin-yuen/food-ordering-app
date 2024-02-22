package view;

/**
 * This class determines for the corresponding topping selection result message to show to the customer based on the
 * topping names entered by the customer.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToppingSelectionResultView {
    public ToppingSelectionResultView() {}

    /**
     * This function determines which ToppingResult view to print out to the customer. If some or all of the requested
     * toppings are not available, the system will print out a relevant message notify the customer that the certain
     * toppings are not valid or in stock.
     *
     * @param toppingsOrder           valid toppings (i.e. currently in stock)
     * @param invalidToppingsOrder    invalid toppings (i.e. currently not in stock)
     */
    public void determineToppingResultView(List<HashMap<String, Double>> toppingsOrder, List<String> invalidToppingsOrder) {

        if (invalidToppingsOrder.size() > 0 && toppingsOrder.size() == 0) {
            printAllInvalidToppingsView();
        } else if (invalidToppingsOrder.size() == 0 && toppingsOrder.size() > 0){
            printAllValidToppingsView();
        } else if (invalidToppingsOrder.size() > 0) {     // and toppingsOrder.size() > 0
            printSomeInvalidToppingsView(toppingsOrder);
        } else {  // customer did not enter any input for topping selection
            System.out.println("Please enter at least one topping");
        }
    }

    /**
     * This function compiles all the ordered toppings that are currently in stock and prints out those topping names
     * in the message.
     *
     * @param toppingsOrder   valid toppings (i.e. currently in stock)
     */
    private void printSomeInvalidToppingsView(List<HashMap<String, Double>> toppingsOrder) {
        String validToppings = "";
        for (var tElement : toppingsOrder) {
            for (Map.Entry<String, Double> tMapElement : tElement.entrySet()) {
                String toppingName = tMapElement.getKey();

                if (toppingsOrder.size() > 2) {
                    if (toppingsOrder.indexOf(tElement) < toppingsOrder.size()-2) {
                        toppingName = toppingName.concat(", ");
                    } else if (toppingsOrder.indexOf(tElement) == toppingsOrder.size()-2) {
                        toppingName = toppingName.concat(" and ");
                    }
                } else if (toppingsOrder.size() == 2) {
                    if (toppingsOrder.indexOf(tElement) == 0) toppingName = toppingName.concat(" and ");
                }

                validToppings += toppingName;
            }
        }

        System.out.println(String.format("We're sorry. We've only added %s. We don't have some of those toppings you requested.", validToppings));
    }

    /**
     * This function prints out a message notifies the customer that some or all of the requested toppings are
     * currently not in stock.
     */
    private void printAllInvalidToppingsView() {
        System.out.println("We're sorry. We don't have those toppings in stock.");
    };

    /**
     * This function prints out a message notifies the customer that all the requested toppings have been added to
     * the Burgers, Sandwiches, or Dogs food item.
     */
    private void printAllValidToppingsView() {
        System.out.println("The toppings have been added.");
    }
}
