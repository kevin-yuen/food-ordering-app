package component;

/**
 * This class contains the specific functions that a customer can perform in the system.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import controller.ServerController;
import general.General;
import service.Global;

import java.util.*;

public class Customer extends Person {
    Scanner scanner = new Scanner(System.in);

    public Customer() {}

    /**
     * This function captures the details of the food that the customer orders and the order quantity. Food details
     * include food name, item category name, and food price.
     *
     * Prior to order fulfillment (i.e. order is added to cart), the current remaining quantity of the food will
     * be checked to ensure that there is sufficient quantity to fulfillment the order request.
     *
     * If customer orders Burgers, Dogs, or Sandwiches item, he will optionally be asked to enter one or more
     * Toppings items, and the current remaining quantity of the selected Topping item(s) will be checked as well
     * before capturing the name and price of the selected topping(s).
     *
     * CartForm object will then be created with the details of the food that the customer orders and the order details.
     * The global variable, menuHashMap, will also be updated according to the latest details of the food retrieved from
     * DB.
     *
     * @param serverController  serverController object
     * @param cartForm          details of the food that customer orders and the order details
     */
    public void createCartForm(ServerController serverController, CartForm cartForm) {
        String itemName = cartForm.getItemType(), foodNameRequest = cartForm.getFoodName();
        int reqQuantity = cartForm.getReqQuantity();
        List<HashMap<String, Double>> tempToppings = cartForm.getToppings(), toppings = new LinkedList<>();

        // update remain quantity of burgers, dogs, sandwiches, fries, milkshake mix-ins, and drinks items
        HashMap<String, Food> dbResponse = serverController.notifyCartModelToUpdateRemainQty(itemName, foodNameRequest, reqQuantity);   // {itemName=Food}
        double foodPrice = dbResponse.get(itemName).getPrice();

        // special handling to update remain quantity and retrieve topping price of toppings items
        if (tempToppings.size() > 0) {
            for (var topping : tempToppings) {
                for (Map.Entry<String, Double> tEntry : topping.entrySet()) {
                    HashMap<String, Food> toppingDBResponse = serverController.notifyCartModelToUpdateRemainQty("Toppings", tEntry.getKey(), 1);
                    double toppingPrice = toppingDBResponse.size() > 0 ? toppingDBResponse.get("Toppings").getPrice() : 0.0d;

                    if (toppingDBResponse.size() >= 1) {
                        Global.syncMenuHashMap(toppingDBResponse);
                        toppings.add(new HashMap<>() {
                            {put(tEntry.getKey(), toppingPrice);}
                        });
                    }
                }
            }
            // param "List<Object> toppings" now contains {toppingName=toppingPrice}
            cartForm = new CartForm(itemName, foodNameRequest, foodPrice, toppings);
        }

        if (dbResponse.size() >= 1) {
            Global.syncMenuHashMap(dbResponse);
            cartForm.writeToCart(cartForm);
        }
    }

    /**
     * This function requests user enter a valid order quantity of the food. "Valid" means that the quantity must be
     * at least 1.
     *
     * @return the order quantity of the food
     */
    public int requestQuantity() {
        int orderedQuantity = 0;

        while (orderedQuantity <= 0) {
            System.out.printf("Enter quantity: ");
            String tempOrderedQuantity = scanner.nextLine();

            try {
                orderedQuantity = Integer.parseInt(tempOrderedQuantity);

                if (orderedQuantity < 0) System.out.println("Quantity must be at least 1.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please try again.");
            }
        }
        return orderedQuantity;
    }

    /**
     * This function asks customer whether he wishes to add any topping(s) to the Burgers, Dogs, or Sandwiches food item.
     * If the customer wishes to proceed with toppings add-on, he will be asked to enter topping name(s). If the
     * customer doesn't wish to proceed, the ordering process will be complete.
     *
     * The given topping item option will be mapped with the toppings stored in the global variable, menuHashMap,
     * using one of the following mapping methods:
     * - if user provides numeric input, the system will find the corresponding topping name by subtracting input
     * number by 1 to find the index position of the topping in the Toppings list.
     *      Example:
     *          numeric input       Toppings list           search result
     *               2         [Mayo, Lettuce, Pickle]         Lettuce
     * - if user provides alphabetical input, the system will match for the topping with the given input by mapping with
     * the topping's unique key for the topping (refer to General.composeToppingNameCharMapping()).
     *      Example:
     *        alphabetical input    Toppings list           search result
     *              l         [Mayo, Lettuce, Pickle]         Lettuce
     *
     * Once toppings are provided, ToppingSelectionResult view will be rendered to print out the result of adding
     * topping(s) to the Burgers, Dogs, or Sandwiches food item.
     *
     * @param   serverController    serverController object
     * @return                      possible returns
     *                                  all valid toppings with topping name and the corresponding price
     *                                  empty toppingsOrder object if no valid topping
     */
    public List<HashMap<String, Double>> requestToppings(ServerController serverController) {
        List<HashMap<String, Double>> toppingsOrder = new LinkedList<>();
        List<String> invalidToppingsOrder = new LinkedList<>();
        boolean addTopping = true;

        do {
            System.out.println("Would you like to add any topping? (\u001B[1my\u001B[0m/\u001B[1mn\u001B[0m)");
            String proceedAddTopping = scanner.nextLine().trim();

            if (proceedAddTopping.length() == 1) {
                Character proceed = proceedAddTopping.charAt(0);

                if (proceed.equals('y')) {
                    String viewRendered = serverController.renderToppingsView();
                    System.out.println(viewRendered);

                    String toppingsReq = scanner.nextLine();
                    String[] tempToppingsOrder = toppingsReq.trim().toLowerCase().split("\\s*,[,\\s]*");
                    Map<String, List<Food>> toppingDets = Global.getMenuHashMap().get("Toppings");
                    List<String> toppingNames = new ArrayList<>(toppingDets.keySet());
                    System.out.println(Global.getMenuHashMap());

                    HashMap<String, String> toppingNameCharMapping = General.composeToppingNameCharMapping(toppingNames);

                    // verify toppings request
                    for (var order : tempToppingsOrder) {
                        boolean isToppingOrderInStock = false;

                        try {
                            // attempt to map for the topping name by topping number
                            int orderByNumber = Integer.parseInt(order);

                            for (var toppingName : toppingNames) {
                                if (toppingNames.indexOf(toppingName) == orderByNumber-1 &&
                                        toppingDets.get(toppingName).get(0).getRemainQty() > 0) {
                                    toppingsOrder.add(new HashMap<>() {
                                        {put(toppingName, 0.0);}
                                    });
                                    isToppingOrderInStock = true;
                                }
                            }
                        } catch (NumberFormatException e) {
                            // else, map for the topping name by topping key
                            for (Map.Entry<String, String> nameChar: toppingNameCharMapping.entrySet()) {
                                String tName = nameChar.getKey();

                                if (order.equalsIgnoreCase(nameChar.getValue()) && toppingDets.get(tName).get(0).getRemainQty() > 0) {
                                    toppingsOrder.add(new HashMap<>() {
                                        {put(tName, 0.0);}
                                    });
                                    isToppingOrderInStock = true;
                                }
                            }
                        }

                        if (!isToppingOrderInStock) invalidToppingsOrder.add(order);    // requested topping is invalid if out of stock or not a valid option
                    }

                    serverController.renderToppingSelectionResultView(toppingsOrder, invalidToppingsOrder);
                    break;
                } else if (proceed.equals('n')) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter '\u001B[1my\u001B[0m' or '\u001B[1mn\u001B[0m'.");
                }
            } else {
                System.out.println("Please enter '\u001B[1my\u001B[0m' or '\u001B[1mn\u001B[0m'.");
            }
        } while(addTopping);
        return toppingsOrder;
    }

    /**
     * This function captures the payment amount entered by the customer.
     *
     * @return the payment amount entered by the customer
     */
    public String makePayment() {
        return scanner.nextLine();
    }

    public String getName() {return "";}
}
