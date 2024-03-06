package component;

/**
 * This class contains functions that an admin can perform in the system.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.*;

public class Admin extends Person {
    private String name;
    Scanner scanner = new Scanner(System.in);

    public Admin(String name) {
        this.name = name;
    }

    /**
     * This function captures the food name which user wants to perform operation on and the new price of the
     * given food name.
     *
     * @return the food name and its new price
     */
    public HashMap<String, Double> requestFoodPriceUpdate() {
        String foodName = requestUserFood();
        double foodPrice = getRequestedPrice();
        HashMap<String, Double> foodNameAndPrice = new HashMap<>() {
            {put(foodName, foodPrice);}
        };
        return foodNameAndPrice;
    }

    /**
     * This function allows user add a new food item under the specified item category by requesting user to provide
     * the followings and compiling those details in the form of Array of Objects.
     * - a new food name
     * - the price of the new food and
     * - the max. quantity of the new food
     *
     * @return the food details (i.e. new food name, price, max. qty) in the form of List of Generics.
     */

    public List<?> addFood() {
        String foodName = requestUserFood();
        double price = getRequestedPrice();
        int maxQty = getRequestedMaxQty();

        List<?> foodDets = Arrays.asList(foodName, price, maxQty);
        return foodDets;
    }

    /**
     * This function requests user provide the name of an existing food and a new, valid max. quantity of the food.
     *
     * @return the food name and the new max. quantity to be updated to
     */
    public HashMap<String, Integer> updateFoodMaxQty() {
        String foodName = requestUserFood();
        int foodMaxQty = getRequestedMaxQty();
        HashMap<String, Integer> foodNameAndMaxQty = new HashMap<>() {
            {put(foodName, foodMaxQty);}
        };
        return foodNameAndMaxQty;
    }

    /**
     * This function requests user provide a new, valid price of the food which he wants to perform operation on.
     *
     * @return the new food price that user provides
     */
    private double getRequestedPrice() {
        boolean isFoodPriceValid = false;
        double foodPrice = 0.0d;

        do {
            System.out.print("Enter food price: ");

            try {
                foodPrice = Double.parseDouble(scanner.nextLine());

                if (foodPrice > 0 && foodPrice < 1000) {
                    isFoodPriceValid = true;
                }
                else if (foodPrice >= 1000) {
                    System.out.println("What the heck?! The food will be overpriced. " +
                            "No one is gonna buy it. Please set a more realistic price.");
                }
                else {
                    System.out.println("Set up a valid price so we can make some profit!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please set the price.");
            }
        } while(!isFoodPriceValid);
        return foodPrice;
    }

    /**
     * This function requests user provide a new, valid max. quantity of the food which he wants to perform operation on.
     *
     * @return the new max. quantity
     */
    private int getRequestedMaxQty() {
        boolean isFoodMaxQtyValid = false;
        int foodMaxQty = 0;

        do {
            System.out.print("Enter max. quantity in stock: ");

            try {
                foodMaxQty = Integer.parseInt(scanner.nextLine());

                if (foodMaxQty >= 1 && foodMaxQty <= 999) {
                    isFoodMaxQtyValid = true;
                }
                else {
                    System.out.println("Max. quantity can only be within the range of 1-999.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please set the max. quantity.");
            }
        } while (!isFoodMaxQtyValid);
        return foodMaxQty;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
