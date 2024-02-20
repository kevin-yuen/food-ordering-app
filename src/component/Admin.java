package component;

/**
 * This class holds the specific functions that an admin can perform in the system.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.HashMap;
import java.util.Scanner;

public class Admin extends Person {
    Scanner scanner = new Scanner(System.in);

    public Admin(String name) {
        super(name);
    }

    /**
     * Capture the food name and its new price
     *
     * This function captures the name of the food which user wants to perform operation on and the new price of the
     * given food name.
     *
     * @return      the food name and its new price
     */
    public HashMap<String, Double> updateFoodPrice() {
        String foodName = requestUserFood();
        double foodPrice = getRequestedPrice();
        HashMap<String, Double> foodNameAndPrice = new HashMap<>() {
            {put(foodName, foodPrice);}
        };
        return foodNameAndPrice;
    }

    public Object[] addFood() {
        String foodName = requestUserFood();
        double price = getRequestedPrice();
        int maxQty = getRequestedMaxQty();

        Object[] foodDetails = {foodName, price, maxQty};
        return foodDetails;
    }

    public HashMap<String, Integer> updateFoodMaxQty() {
        String foodName = requestUserFood();
        int foodMaxQty = getRequestedMaxQty();
        HashMap<String, Integer> foodNameAndMaxQty = new HashMap<>() {
            {put(foodName, foodMaxQty);}
        };
        return foodNameAndMaxQty;
    }

    /**
     * Request user to provide a new price of the food which he wants to perform operation on.
     *
     * This function requests user to provide a new, valid price of the food which he wants to perform operation on.
     *
     * @return      the new food price that user provides
     */
    private double getRequestedPrice() {
        double foodPrice = 0.0d;

        while (foodPrice <= 0.0) {
            System.out.print("Enter food price: ");

            try {
                foodPrice = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please set the price.");
                continue;
            }

            if (foodPrice <= 0.0) {
                System.out.println("You haven't set the price yet.");
                continue;
            }
            break;
        }
        return foodPrice;
    }

    private int getRequestedMaxQty() {
        int foodMaxQty = 0;

        while (foodMaxQty <= 0) {
            System.out.print("Enter max. quantity in stock: ");

            try {
                foodMaxQty = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please set the max. quantity.");
                continue;
            }

            if (foodMaxQty <= 0) {
                System.out.println("You haven't set the max. quantity yet.");
                continue;
            }
            break;
        }
        return foodMaxQty;
    }
}
