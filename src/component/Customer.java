package component;

import controller.ServerController;
import general.General;
import service.Global;

import java.util.*;

public class Customer extends Person {
    Scanner scanner = new Scanner(System.in);

    public Customer(String name) {
        super(name);
    }

    // Check whether the food that customer orders is in stock
    //
    // This function checks whether sufficient quantity in stock to fulfill customer's order by checking against the db.
    //
    // @param cartForm = customer's latest order request details
    //
    public void createCartForm(ServerController serverController, CartForm cartForm) {
        String itemName = cartForm.getItemType(), foodNameRequest = cartForm.getFoodName();
        int reqQuantity = cartForm.getReqQuantity();
        List<HashMap<String, Double>> tempToppings = cartForm.getToppings(), toppings = new LinkedList<>();

        // update remain quantity of burgers, dogs, sandwiches, fries, milkshake mix-ins, and drinks items
        HashMap<String, Food> dbResponse = serverController.notifyCartModelToUpdateRemainQty(itemName, foodNameRequest, reqQuantity);   // {itemName=Food}
        double foodPrice = dbResponse.get(itemName).getPrice();

        // special handling to update remain quantity and retrieve topping price of toppings items
        if (tempToppings.size() > 0) {
            for (var topping: tempToppings) {
                for (Map.Entry<String, Double> tEntry: topping.entrySet()) {
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

    public int requestQuantity() {
        int orderedQuantity = 0;

        while (orderedQuantity <= 0) {
            System.out.printf("Enter quantity: ");
            String tempOrderedQuantity = scanner.nextLine();

            try {
                orderedQuantity = Integer.parseInt(tempOrderedQuantity);

                if (orderedQuantity < 0) System.out.println("Quantity must be at least 1.");
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please try again.");
            }
        }
        return orderedQuantity;
    }

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
                    for (var order: tempToppingsOrder) {
                        boolean isToppingOrderInStock = false;

                        try {
                            // attempt to map for the topping name by topping number
                            int orderByNumber = Integer.parseInt(order);

                            for (var toppingName: toppingNames) {
                                if (toppingNames.indexOf(toppingName) == orderByNumber-1 &&
                                        toppingDets.get(toppingName).get(0).getRemainQty() > 0) {
                                    toppingsOrder.add(new HashMap<>() {
                                        {put(toppingName, 0.0);}
                                    });
                                    //toppingsOrder.add(toppingName);
                                    isToppingOrderInStock = true;
                                }
                            }
                        }
                        catch (NumberFormatException e) {
                            // else, map for the topping name by topping key
                            for (Map.Entry<String, String> nameChar: toppingNameCharMapping.entrySet()) {
                                String tName = nameChar.getKey();

                                if (order.equalsIgnoreCase(nameChar.getValue()) && toppingDets.get(tName).get(0).getRemainQty() > 0) {
                                    toppingsOrder.add(new HashMap<>() {
                                        {put(tName, 0.0);}
                                    });
                                    //toppingsOrder.add(tName);
                                    isToppingOrderInStock = true;
                                }
                            }
                        }

                        if (!isToppingOrderInStock) invalidToppingsOrder.add(order);    // requested topping is invalid if out of stock or not a valid option
                    }

                    serverController.renderToppingSelectionResultView(toppingsOrder, invalidToppingsOrder);
                    break;
                }
                else if (proceed.equals('n')) {
                    break;
                }
                else {
                    System.out.println("Invalid input. Please enter '\u001B[1my\u001B[0m' or '\u001B[1mn\u001B[0m'.");
                }
            }
            else {
                System.out.println("Please enter '\u001B[1my\u001B[0m' or '\u001B[1mn\u001B[0m'.");
            }
        } while(addTopping);
        //System.out.println("REQUESTTOPPINGS(): " + toppingsOrder);
        return toppingsOrder;
    }

    public String makePayment() {
        return scanner.nextLine();
    }
}
