package general;

/**
 * This class contains general methods that are used by other classes.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.*;

public class General {
    private static String requestedSysOpt;

    public General() {}

    /**
     * Create border around the system name
     *
     * This function creates border around the system name and prints out the border and the system name.
     */
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

    /**
    * Verify user's input on food item selection
    *
    * This function verifies user's input on food item selection and if an alphabetical input is provided, it will
    * convert the input to the corresponding item option number.
    *
    * @param   itemSelected    user's input on food item selection
    * @return                  the corresponding item option number
     */
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

    /**
     * Create unique key for each topping name
     *
     * This function creates a unique key for each topping name. Customer can order a topping by entering the unique
     * identifier of the desired topping.  A unique key is defined by the initial letter of the topping name.  If
     * the topping name is composed of 2 words, a unique key is defined by the initial letter of each word.
     *
     * Example:
     *  topping name = Mustard >>> unique key of Mustard = m
     *  topping name = Grilled Mushroom >>> unique key of Grilled Mushroom = gm
     *
     * @param   toppingNames    topping name in the global variable, menuHashMap
     * @return                  the unique key of each topping name and the topping name
     */
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

    /**
     * Capture user's selected option
     *
     * This function captures user's selected option throughout the program (for example, Operation menu).
     */
    public static void setRequestedSysOpt() {
        Scanner scanner = new Scanner(System.in);
        requestedSysOpt = scanner.nextLine();
    }

    /**
     * Retrieve user's selected option
     *
     * This function retrieves user's selected option throughout the program (for example, Operation menu).
     */
    public static String getRequestedSysOpt() {
        return requestedSysOpt;
    }
}
