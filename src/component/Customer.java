package component;

import controller.ServerController;

import java.util.HashMap;
import java.util.Scanner;

public class Customer extends Person {
    Scanner scanner = new Scanner(System.in);
    private int reqQuantity;

    public Customer(String name) {
        super(name);
    }

    // fries, drinks, milkshake
    public HashMap<String, Food> addNonBreadItemToCart(ServerController serverController, String itemName, String foodNameRequest) {
        // create a new function, notifyMenuModelToUpdateRemainQty(itemName, foodName, reqQty), in servercontroller
        // create a new function, sendDBRequestToUpdateRemainQty(itemName, foodName, reqQty), menu model
        // create a new function, retrieveLatestRemainQty(itemName, foodName), in database
        // send requested item name, food name, and quantity to servercontroller
        // notifyMenuModelToUpdateRemainQty(itemName, foodName, reqQty) from servercontroller pass request to Menu Model
        // from Menu Model, call db.updateFoodDetail(String sql, String arg, String name, String price, String remainQty, String maxQty)
        setReqQuantity();
        int reqQuantity = getReqQuantity();
        return serverController.notifyMenuModelToUpdateRemainQty(itemName, foodNameRequest, reqQuantity);
    }

    private void setReqQuantity() {
        int orderedQuantity = 0;

        while (orderedQuantity <= 0) {
            System.out.printf("Enter quantity: ");
            String tempOrderedQuantity = scanner.nextLine();

            try {
                orderedQuantity = Integer.parseInt(tempOrderedQuantity);

                if (orderedQuantity < 0) System.out.println("Quantity must be at least 1.");
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please try again.");
            }
        }
        this.reqQuantity = orderedQuantity;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public void removeItemFromCart() {

    }

    public void updateItemInCart() {

    }

    public void makePayment() {

    }

//    private void setReqQuantity(int reqQuantity) {
//        this.reqQuantity = reqQuantity;
//    }
}
