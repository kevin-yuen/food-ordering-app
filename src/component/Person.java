package component;

import controller.ServerController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Person {
    Scanner scanner = new Scanner(System.in);
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public void viewMenu(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap, ServerController serverController) {
        serverController.renderMenuView(foodHashMap);
    }

    public String requestUserFood() {
        String tmpFoodName;
        String foodName = "";

        while (true) {
            System.out.print("Enter food name: ");
            tmpFoodName = scanner.nextLine().trim().toLowerCase();

            if (tmpFoodName.equalsIgnoreCase("")) {
                System.out.println("Please enter food name.");
                continue;
            }
            else {
                String[] tmpFoodNameToClean = tmpFoodName.split(" ");
                for (var name: tmpFoodNameToClean) {
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
