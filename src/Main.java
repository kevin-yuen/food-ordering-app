import component.Admin;
import component.Food;
import component.Menu;
import source.Database;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        Main main = new Main();
        Admin admin = new Admin();
        Menu menu = new Menu();

        HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap;

        db.createDBConnection();
        main.drawBoard();

        while (true) {
            // get the latest menu
            menu.composeLatestMenu(db);
            foodHashMap = menu.getFoodHashMap();

            int operationCde = 0;   // 0 = no operation; 1 = update menu; 2 = take order

            // 1. System asks admin to enter the operation
            while (operationCde == 0) {
                System.out.println("Enter the operation you want to perform:" +
                        "\n1. [\u001B[1mU\u001B[0m]pdate menu" +
                        "\n2. [\u001B[1mT\u001B[0m]ake order");
                admin.enterSystemOption();
                String operation = admin.getMyRequestedSysOpt();

                try {
                    int tmpOperationCde = Integer.parseInt(operation);
                    operationCde = tmpOperationCde == 1 ? 1 : (tmpOperationCde == 2 ? 2 : 0);
                }
                catch (NumberFormatException e) {
                    operationCde = operation.equalsIgnoreCase("u") ? 1 :
                            (operation.equalsIgnoreCase("t") ? 2 : 0);
                }

                if (operationCde == 0) System.out.println("Invalid operation. Please try again.");
            }

            // update menu operation
            if (operationCde == 1) {
                ArrayList<String> itemList = new ArrayList<>(foodHashMap.keySet());
                String itemSelectMessage = "";
                String styledItemName;
                int itemNumber = 1;

                for (var item: itemList) {
                    // style item name
                    if (!item.equalsIgnoreCase("drinks")) {
                        styledItemName = "[\u001B[1m" + item.substring(0,1) + "\u001B[0m]" + item.substring(1);
                    }
                    else {
                        styledItemName = "[\u001B[1m" + item.substring(0,2) + "\u001B[0m]" + item.substring(2);
                    }

                    itemSelectMessage += itemNumber + ". " + styledItemName + "\n";
                    itemNumber++;
                }
                itemSelectMessage += itemNumber + ". " + "<<[\u001B[1m[Ba\u001B[0m]ck to Previous>>\n";
                int itemCde = 0;

                // 2: System asks user to enter the item type to perform operation on
                while (itemCde == 0) {
                    System.out.printf("What kind of item do you want to update?\n%s", itemSelectMessage);
                    admin.enterSystemOption();
                    String itemSelected = admin.getMyRequestedSysOpt();

                    try {
                        int tempItemCde = Integer.parseInt(itemSelected);
                        itemCde = tempItemCde >= 1 && tempItemCde <= 8 ? tempItemCde : 0;
                    }
                    catch (NumberFormatException e) {
                        itemCde = switch (itemSelected.toLowerCase()) {
                            case "b" -> 1;
                            case "d" -> 2;
                            case "dr" -> 3;
                            case "f" -> 4;
                            case "m" -> 5;
                            case "s" -> 6;
                            case "t" -> 7;
                            case "ba" -> 8;
                            default -> 0;
                        };
                    }

                    if (itemCde == 0) System.out.println("Invalid item. Please try again.");

                    if (itemCde >= 1 && itemCde <= 7) {
                        int operationCdeOnItem = 0;

                        // 3. System asks user to enter the operation for the selected item type
                        while (operationCdeOnItem == 0) {
                            System.out.println("What do you want to do with the selected item?" +
                                    "\n1. [\u001B[1mU\u001B[0m]pdate food price\n2. [\u001B[1mA\u001B[0m]dd new food" +
                                    "\n3. [\u001B[1mAd\u001B[0m]just max quantity" +
                                    "\n4. [\u001B[1mN\u001B[0m]ot sure. Let me review the menu." +
                                    "\n5. <<[\u001B[1m[Ba\u001B[0m]ck to Previous>>");
                            admin.enterSystemOption();
                            String operationOnItem = admin.getMyRequestedSysOpt();

                            try {
                                int tempOperationOnItemCde = Integer.parseInt(operationOnItem);
                                operationCdeOnItem = tempOperationOnItemCde >= 1 && tempOperationOnItemCde <= 4 ?
                                        tempOperationOnItemCde : 0;
                            }
                            catch (NumberFormatException e) {
                                operationCdeOnItem = switch (operationOnItem.toLowerCase()) {
                                    case "u" -> 1;
                                    case "a" -> 2;
                                    case "ad" -> 3;
                                    case "n" -> 4;
                                    case "ba" -> 5;
                                    default -> 0;
                                };
                            }

                            if (operationCdeOnItem == 0) System.out.println("Invalid operation. Please try again.");
                        }

                        if (operationCdeOnItem == 1) {

                        }
                        else if (operationCdeOnItem == 2) {

                        }
                        else if (operationCdeOnItem == 3) {

                        }
                        else if (operationCdeOnItem == 4) {
                            admin.viewMenu(foodHashMap);
                        }
                        else if (operationCdeOnItem == 5) {

                        }
                    }
                }
                if (itemCde == 8) {
                    continue;
                }
            }
            // take order operation
            else if (operationCde == 2) {

            }

            break;
        }
    }

    public void drawBoard() {
        String sysName = "FIVE GUYS FOOD ORDERING SYSTEM";
        int boardLineCounter = 1;

        while (true) {
            if (boardLineCounter <= 3) {
                if (boardLineCounter % 2 != 0) {
                    System.out.println("-".repeat(38));
                }
                else {
                    System.out.println("|" + " ".repeat(3) + sysName + " ".repeat(3) + "|");
                }

                boardLineCounter += 1;
            }
            else {
                break;
            }
        }
    }
}

