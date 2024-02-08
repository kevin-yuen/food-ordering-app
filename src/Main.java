import component.Admin;
import component.Customer;
import component.Food;
import controller.ServerController;
import general.General;
import model.MenuModel;
import service.Database;
import service.Global;
import view.MenuView;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int appState = 0;   // 1 = update menu; 2 = take order

        Database db = new Database();

        Admin admin = new Admin("Store Manager");
        Customer customer = new Customer("Customer");
        MenuModel menuModel = new MenuModel(db);
        MenuView menuView = new MenuView();
        ServerController serverController = new ServerController(menuModel, menuView);

        General.drawBoard();

        while (true) {
            String viewRendered = "";
            ArrayList<String> itemList = new ArrayList<>();

            // 1. System asks admin to enter the operation
            while (appState == 0) {
                System.out.println("Enter the operation you want to perform:" +
                        "\n1. [\u001B[1mU\u001B[0m]pdate menu" +
                        "\n2. [\u001B[1mT\u001B[0m]ake order" +
                        "\n3. [\u001B[1mV\u001B[0m]iew cart");
                General.setRequestedSysOpt();
                String operation = General.getRequestedSysOpt();

                try {
                    int tmpOperationCde = Integer.parseInt(operation);
                    appState = tmpOperationCde == 1 ? 1 : (tmpOperationCde == 2 ? 2 : 0);
                }
                catch (NumberFormatException e) {
                    appState = operation.equalsIgnoreCase("u") ? 1 :
                            (operation.equalsIgnoreCase("t") ? 2 : 0);
                }

                if (appState != 0) {
                    Map<String, HashMap<String, List<Food>>> latestMenuItemsFromDB =
                            serverController.notifyMenuModelToGetsFromDB();

                    if (latestMenuItemsFromDB.size() >= 1) {
                        Global.setMenuHashMap(latestMenuItemsFromDB);
                    }
                    else {
                        serverController.renderServerErrorView();
                    }
                    viewRendered = serverController.renderItemView(appState, Global.getMenuHashMap());
                    itemList = new ArrayList<>(Global.getMenuHashMap().keySet());
                }
                else {
                    serverController.renderErrorView();
                }
            }

            // update menu operation
            if (appState == 1) {
                int updateOpItemCde = 0;

                // 2: System asks user to enter the item type to perform operation on
                while (updateOpItemCde == 0) {
                    System.out.print(viewRendered);
                    General.setRequestedSysOpt();
                    updateOpItemCde = General.verifyItemSelection(General.getRequestedSysOpt());

                    if (updateOpItemCde >= 1 && updateOpItemCde <= 7) {
                        String itemName = itemList.get(updateOpItemCde - 1);
                        int operationCdeOnItem = 0;

                        // 3. System asks user to enter the operation for the selected item type
                        while (operationCdeOnItem == 0) {
                            System.out.println("What do you want to do with the selected item?" +
                                    "\n1. [\u001B[1mU\u001B[0m]pdate food price\n2. [\u001B[1mA\u001B[0m]dd new food" +
                                    "\n3. [\u001B[1mAd\u001B[0m]just max quantity" +
                                    "\n4. [\u001B[1mN\u001B[0m]ot sure. Let me review the menu." +
                                    "\n5. <<[\u001B[1m[Ba\u001B[0m]ck to Previous>>");
                            General.setRequestedSysOpt();
                            String operationOnItem = General.getRequestedSysOpt();

                            try {
                                int tempOperationOnItemCde = Integer.parseInt(operationOnItem);
                                operationCdeOnItem = tempOperationOnItemCde >= 1 && tempOperationOnItemCde <= 4 ?
                                        tempOperationOnItemCde : 0;
                            }
                            catch (NumberFormatException e) {
                                operationCdeOnItem = switch (operationOnItem.toLowerCase()) {
                                    case "u" -> 1;  // update food price
                                    case "a" -> 2;  // add new food
                                    case "ad" -> 3; // adjust max quantity of the food
                                    case "n" -> 4;  // review menu
                                    case "ba" -> 5; // back to previous page
                                    default -> 0;
                                };
                            }

                            if (operationCdeOnItem == 1) {
                                HashMap<String, Double> foodNameAndPrice = admin.updateFoodPrice();
                                String foodName = foodNameAndPrice.keySet().toArray()[0].toString();
                                double foodPrice = foodNameAndPrice.get(foodName);

                                HashMap<String, Food> dbResponse =
                                        serverController.notifyMenuModelToUpdatePrice(itemName, foodName, foodPrice);

                                if (dbResponse.size() > 0) {
                                    Global.syncMenuHashMap(dbResponse);
                                }
                                operationCdeOnItem = 0;     // reset to perform other operations on the selected item
                            }
                            else if (operationCdeOnItem == 2) {
                                Object[] foodDetails = admin.addFood();
                                List<Object> foodDetailsObject = Arrays.asList(foodDetails);

                                String foodName = foodDetailsObject.get(0).toString();
                                double price = Double.parseDouble(foodDetailsObject.get(1).toString());
                                int maxQty = Integer.parseInt(foodDetailsObject.get(2).toString());

                                HashMap<String, Food> dbResponse = serverController
                                        .notifyMenuModelToCreateFood(itemName, foodName, price, maxQty);

                                if (dbResponse.size() > 0) {
                                    Global.syncMenuHashMap(dbResponse);
                                }
                                operationCdeOnItem = 0;     // reset to perform other operations on the selected item
                            }
                            else if (operationCdeOnItem == 3) {
                                HashMap<String, Integer> foodNameAndMaxQty = admin.updateFoodMaxQty();
                                String reqFoodName = foodNameAndMaxQty.keySet().toArray()[0].toString();
                                int reqMaxQty = foodNameAndMaxQty.get(reqFoodName);

                                HashMap<String, Food> dbResponse = serverController
                                        .notifyMenuModelToUpdateMaxQty(itemName, reqFoodName, reqMaxQty);

                                if (dbResponse.size() > 0) {
                                    Global.syncMenuHashMap(dbResponse);
                                }
                                operationCdeOnItem = 0;     // reset to perform other operations on the selected item
                            }
                            else if (operationCdeOnItem == 4) {
                                String keyPressed;

                                admin.viewMenu(Global.getMenuHashMap(), serverController);
                                System.out.print("Press any key to go back to previous menu: ");
                                General.setRequestedSysOpt();
                                keyPressed = General.getRequestedSysOpt();

                                if (keyPressed != null) operationCdeOnItem = 0;
                            }
                            else if (operationCdeOnItem == 0) {
                                serverController.renderErrorView();
                            }
                        }

                        if (operationCdeOnItem == 5) {
                            updateOpItemCde = 0;
                            continue;
                        }
                    }
                    else if (updateOpItemCde == 0) {
                        serverController.renderErrorView();
                    }
                }

                if (updateOpItemCde == 8) {
                    appState = 0;   // reset state of the app to default state
                    continue;
                }
            }
            // take order operation
            else if (appState == 2) {
                int takeOrderOpItemCde = 0;
                boolean shouldContOrder = true;
//                ArrayList<HashMap<String, Integer>> cart = new ArrayList<>();
//                HashMap<String, Integer> tempCart;
                HashMap<String, Integer> cart = new HashMap<>();

                Scanner scanner = new Scanner(System.in);

                String adminName = admin.getName();
                String customerName = customer.getName();

                System.out.printf("\u001B[1m%s\u001B[0m: Welcome to Five Guys!\n", adminName);
                customer.viewMenu(Global.getMenuHashMap(), serverController);
                // foodOrdered variable
                // [{foodName=quantity},....]

                // Ask customer to enter a food item from "Fries", "Burgers", "Dogs", "Drinks", "Milkshake Mix-ins", or "Sandwiches"
                    // IF he enters a topping item
                        // THEN ask customer again to enter a food item from "Fries", "Burgers", "Dogs", "Drinks", "Milkshake Mix-ins", or "Sandwiches"
                    // ELSE IF he enters a food item from "Burgers", "Dogs", or "Sandwiches"
                        // THEN ask customer if he wants any topping item
                            // IF yes, ask customer to enter a topping item
                                // THEN ask customer if he wants any more topping item
                                    // IF yes, ask customer again to enter a topping item
                                    // IF no, ask customer to enter a food item from "Fries", "Burgers", "Dogs", "Drinks", "Milkshake Mix-ins", or "Sandwiches"
                            // IF no, ask customer to enter a food item from "Fries", "Burgers", "Dogs", "Drinks", "Milkshake Mix-ins", or "Sandwiches"
                    // ELSE IF he enters a food item from "Fries", "Drinks", or "Milkshake Mix-ins"
                        // THEN ask customer to enter quantity

                while (shouldContOrder) {
                    String tempContOrder;
                    String takeOrderOpItemName = "";

                    while (takeOrderOpItemCde == 0) {
                        System.out.printf("\u001B[1m%s\u001B[0m: %s", adminName, viewRendered);
                        General.setRequestedSysOpt();
                        takeOrderOpItemCde = General.verifyItemSelection(General.getRequestedSysOpt());

                        if (takeOrderOpItemCde >= 1 && takeOrderOpItemCde <= 6) {
                            takeOrderOpItemName = itemList.get(takeOrderOpItemCde - 1);
                        }
                        else if (takeOrderOpItemCde == 7 || takeOrderOpItemCde == 8) {
                            appState = 0;
                        }
                        else if (takeOrderOpItemCde == 0) {
                            serverController.renderErrorView();
                        }
                    }

                    if (appState == 0) {
                        shouldContOrder = false;
                        break;
                    }

                    String foodNameRequest = customer.requestUserFood();

                    // {Food A=2, Food B=5}

                    if (takeOrderOpItemCde == 1 || takeOrderOpItemCde == 4 || takeOrderOpItemCde == 5) {
                        HashMap<String, Food> foodToSyncWithGlobal = customer.addNonBreadItemToCart(serverController, takeOrderOpItemName, foodNameRequest);

                        if (foodToSyncWithGlobal.size() >= 1) {

//                            String itemName = foodToSyncWithGlobal.keySet().toArray()[0].toString(),
//                                    foodName = foodToSyncWithGlobal.get(itemName).getFoodName();
//                            int custReqQuantity = customer.getReqQuantity();
//                            tempCart = new HashMap<>();
//
//                            tempCart.put(foodName, custReqQuantity);
//
//                            for (Map.Entry foodItem: cart.entrySet()) {
//                                if (foodItem.getKey() == foodName) {
//                                    int currentQuantity = Integer.parseInt((String) foodItem.getValue());
//                                    foodItem.setValue(currentQuantity + 1);
//                                }
//                            }
//                            foodsOrdered.add(foodOrderedHashMap);
//                            Global.syncMenuHashMap(foodToSyncWithGlobal);
//                            System.out.println("Temp cart: " + foodsOrdered);
                        }
                    }
                    // burgers, dogs, sandwiches
                    else if (takeOrderOpItemCde == 2 || takeOrderOpItemCde == 3 || takeOrderOpItemCde == 6) {
                        // ask for toppings
                    }
//                    else if (takeOrderOpItemCde == 7) {
//                        appState = 0;
//                    }
                    else if (takeOrderOpItemCde == 0) {
                        serverController.renderErrorView();
                        continue;
                    }

                    do {
                        System.out.println("Do you want to order anything else? (\u001B[1my\u001B[0m/\u001B[1mn\u001B[0m)");
                        tempContOrder = scanner.nextLine().trim();

                        if (tempContOrder.length() == 1) {
                            Character contOrder = tempContOrder.charAt(0);

                            if (contOrder.equals('y')) {
                                takeOrderOpItemCde = 0;     // redirect back to the item selection view
                                break;
                            }
                            else if (contOrder.equals('n')) {
                                shouldContOrder = false;
                                break;
                            }
                            else {
                                System.out.println("Invalid input. Please enter '\u001B[1my\u001B[0m/' or '\u001B[1mn\u001B[0m/'.");
                                tempContOrder = "";
                            }
                        }
                        else {
                            System.out.println("Please enter '\u001B[1my\u001B[0m/' or '\u001B[1mn\u001B[0m/'.");
                            tempContOrder = "";
                        }
                    } while(tempContOrder == "");
                }
                //if (!shouldContOrder) continue;
            }
        }
    }
}

