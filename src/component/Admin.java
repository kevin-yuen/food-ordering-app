package component;

import java.util.HashMap;
import java.util.Scanner;

public class Admin extends Person {
    Scanner scanner = new Scanner(System.in);

    public Admin(String name) {
        super(name);
    }

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
