package general;

// This class contains general methods that are used by other classes.

import java.util.Scanner;
import java.util.Optional;

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

    public static String calculateExtraSpaceToAdd(String value, int maxLenOfAllValues, int totalLen) {
        String refomattedValue = null;

        if (value.length() < maxLenOfAllValues) {
            refomattedValue = value.concat(" ".repeat(totalLen - value.length()));
        }
        return refomattedValue;
    }

    public static String createKeyValueForPriceUpdate(String foodName, double price) {
        return String.format("%s:%s", foodName, price);
    }

    public static String createKeyValueForQuantityUpdate(String foodName, int quantity) {
        return String.format("%s:%s", foodName, quantity);
    }

    public static void setRequestedSysOpt() {
        Scanner scanner = new Scanner(System.in);
        requestedSysOpt = scanner.nextLine();
    }

    public static String getRequestedSysOpt() {
        return requestedSysOpt;
    }
}
