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
        db.createDBConnection();

        Admin admin = new Admin("Store Manager");
        Customer customer = new Customer("Customer");
        MenuModel menuModel = new MenuModel(db);
        MenuView menuView = new MenuView();
        ServerController serverController = new ServerController(menuModel, menuView);

        General.drawBoard();

        while (true) {
            String viewRendered = null;

            // 1. System asks admin to enter the operation
            while (appState == 0) {
                System.out.println("Enter the operation you want to perform:" +
                        "\n1. [\u001B[1mU\u001B[0m]pdate menu" +
                        "\n2. [\u001B[1mT\u001B[0m]ake order");
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
                    Global.setMenuHashMap(serverController.notifyMenuModelToGetsFromDB());
                    viewRendered = serverController.renderMainView(appState, Global.getMenuHashMap());

                    System.out.println("CURRENT MENU HASH MAP: " + Global.getMenuHashMap());
                }
                else {
                    serverController.renderErrorView();
                }
            }

            // update menu operation
            if (appState == 1) {
                ArrayList<String> itemList = new ArrayList<>(Global.getMenuHashMap().keySet());
                int itemCde = 0;

                // 2: System asks user to enter the item type to perform operation on
                while (itemCde == 0) {
                    System.out.print(viewRendered);
                    General.setRequestedSysOpt();
                    String itemSelected = General.getRequestedSysOpt();

                    try {
                        int tempItemCde = Integer.parseInt(itemSelected);
                        itemCde = tempItemCde >= 1 && tempItemCde <= 8 ? tempItemCde : 0;
                    }
                    catch (NumberFormatException e) {
                        itemCde = switch (itemSelected.toLowerCase()) {
                            case "f" -> 1;
                            case "b" -> 2;
                            case "d" -> 3;
                            case "dr" -> 4;
                            case "m" -> 5;
                            case "s" -> 6;
                            case "t" -> 7;
                            case "ba" -> 8;
                            default -> 0;
                        };
                    }

                    if (itemCde >= 1 && itemCde <= 7) {
                        String itemName = itemList.get(itemCde - 1);
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
                                String priceUpdateRequest = admin.updateFoodPrice();
                                String foodName = priceUpdateRequest.substring(0, priceUpdateRequest.indexOf(':'));
                                double foodPrice = Double.parseDouble(priceUpdateRequest.substring(
                                        priceUpdateRequest.indexOf(':')+1));

                                HashMap<String, ArrayList<Food>> dbResponse =
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

                                HashMap<String, ArrayList<Food>> dbResponse = serverController
                                        .notifyMenuModelToCreateFood(itemName, foodName, price, maxQty);

                                if (dbResponse.size() > 0) {
                                    Global.syncMenuHashMap(dbResponse);
                                }
                                operationCdeOnItem = 0;     // reset to perform other operations on the selected item
                            }
                            else if (operationCdeOnItem == 3) {
                                String maxQtyUpdateRequest = admin.updateFoodMaxQty();
                                String reqFoodName = maxQtyUpdateRequest.substring(0, maxQtyUpdateRequest.indexOf(':'));
                                int reqMaxQty = Integer.parseInt(maxQtyUpdateRequest.substring(
                                        maxQtyUpdateRequest.indexOf(':')+1));

                                HashMap<String, ArrayList<Food>> dbResponse = serverController
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
                            itemCde = 0;
                            continue;
                        }
                    }
                    else if (itemCde == 0) {
                        serverController.renderErrorView();
                    }
                }

                if (itemCde == 8) {
                    appState = 0;   // reset state of the app to default state
                    continue;
                }
            }
            // take order operation
            else if (appState == 2) {
                String adminName = admin.getName();
                String customerName = customer.getName();

                System.out.printf("\u001B[1m%s\u001B[0m: Welcome to Five Guys, what would you like to have?\n", adminName);
                customer.viewMenu(Global.getMenuHashMap(), serverController);

                while (true) {
                    ArrayList<HashMap<String, Integer>> foodsOrdered = new ArrayList<>();
                    HashMap<String, Integer> foodOrdered = new HashMap<>();

                    // foodOrdered
                    // [{foodName=quantity},....]

                    break;
                }
            }

            break;
        }
    }
}

