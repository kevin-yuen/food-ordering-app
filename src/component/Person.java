package component;

/**
 * This class is the parent class of Admin class and Customer class.  It holds common attributes and functions used by
 * the Admin class and Customer class.
 * Common attribute     name
 * Common functions     viewMenu()
 *                      requestUserFood()
 *                      getName()
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import controller.ServerController;

import java.util.*;

public class Person {
    Scanner scanner = new Scanner(System.in);
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void viewMenu(Map<String, HashMap<String, List<Food>>> foodHashMap, ServerController serverController) {
        serverController.renderMenuView(foodHashMap);
    }

    /**
     * Request user to provide the food name which he wants to perform operation on.
     *
     * This function requests user to enter the name of the food which he wants to perform operation on and reformat
     * user's input, so the format of given input value will be standardized in the DB.
     *
     * @return      the food name that user enters
     */
    public String requestUserFood() {
        String tmpFoodName;
        String foodName = "";

        while (true) {
            System.out.print("Enter food name: ");
            tmpFoodName = scanner.nextLine().trim().toLowerCase();

            if (tmpFoodName.equalsIgnoreCase("")) {
                System.out.println("Please enter food name.");
                continue;
            } else {
                // reformat user's input so the value format will be standardized (e.g. Five Guys Style)
                String[] tmpFoodNameToClean = tmpFoodName.split(" ");
                for (var name : tmpFoodNameToClean) {
                    foodName += Character.toString(name.charAt(0)).toUpperCase() + name.substring(1) + " ";
                }
                foodName = foodName.trim();     // remove the last space
            }
            break;
        }
        return foodName;
    }

    public String getName() {
        return this.name;
    }
}
