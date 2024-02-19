package general;

// This class contains general methods that are used by other classes.

import java.util.*;

public class General {
    private static String requestedSysOpt;

    public General() {}

    public static void drawBoard() {
        String sysName = "FIVE GUYS FOOD ORDERING SYSTEM";
        int boardLineCounter = 1;

        while (true) {
            if (boardLineCounter <= 3) {
                if (boardLineCounter % 2 != 0) {
                    System.out.println("-".repeat(38));
                } else {
                    System.out.println("|" + " ".repeat(3) + sysName + " ".repeat(3) + "|");
                }

                boardLineCounter += 1;
            } else {
                break;
            }
        }
    }

    public static String calculateExtraSpaceToAdd(String value, int maxLenOfAllValues, int totalLen) {
        String refomattedValue = null;

        if (value.length() < maxLenOfAllValues) {
            refomattedValue = value.concat(" ".repeat(totalLen - value.length()));
        }
        return refomattedValue;
    }

    public static int verifyItemSelection(String itemSelected) {
        int itemCde;

        try {
            int tempItemCde = Integer.parseInt(itemSelected);
            itemCde = tempItemCde >= 1 && tempItemCde <= 8 ? tempItemCde : 0;
        } catch (NumberFormatException e) {
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
        return itemCde;
    }

    public static HashMap<String, String> composeToppingNameCharMapping(List<String> toppingNames) {
        HashMap<String, String> nameCharMapping = new HashMap<>();

        for (var toppingName : toppingNames) {
            String toppingChar = "";
            if (toppingName.equalsIgnoreCase("Grilled Onions") || toppingName.equalsIgnoreCase("Grilled Mushrooms")) {
                String[] tempToppingName = toppingName.split(" ");

                for (var tName : tempToppingName) {
                    toppingChar += tName.toLowerCase().substring(0, 1);
                }
            } else if (toppingName.equalsIgnoreCase("Mustard")) {
                toppingChar = toppingName.toLowerCase().substring(0, 2);
            } else {
                toppingChar = toppingName.toLowerCase().substring(0, 1);
            }
            nameCharMapping.put(toppingName, toppingChar);
        }
        return nameCharMapping;
    }

    public static void setRequestedSysOpt() {
        Scanner scanner = new Scanner(System.in);
        requestedSysOpt = scanner.nextLine();
    }

    public static String getRequestedSysOpt() {
        return requestedSysOpt;
    }
}
