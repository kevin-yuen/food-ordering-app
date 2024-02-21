package component;

/**
 * This class acts as an intermediary between customer and cart.  When a customer requests to order a food item each
 * time, cartForm object will be called and it will capture:
 * 1. the details of the food that customer orders
 * 2. the customer's order request details
 *
 * Once cartForm object is created with the above details, the object will be recorded in Cart class.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CartForm {
    private String itemType;
    private String foodName;
    private double foodPrice;   // price for 1 quantity
    private int reqQuantity;
    private List<HashMap<String, Double>> toppings = new LinkedList<>();

    public CartForm() {}

    // Fries, Drinks, or Milkshake
    public CartForm(String itemType, String foodName, double foodPrice, int reqQuantity) {
        this.itemType = itemType;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.reqQuantity = reqQuantity;
    }

    // Burgers, Dogs, or Sandwiches
    public CartForm(String itemType, String foodName, double foodPrice, List<HashMap<String, Double>> toppings) {
        this.itemType = itemType;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.reqQuantity = 1;
        this.toppings = toppings;
    }

    /**
     * Record the cartForm object in Cart
     *
     * This function creates the cartForm object in Cart class by calling the class name as setTempCart() is a static
     * method. The cartForm object contains details of the food, such as food name, price, item category name,
     * order quantity, and/or topping name(s) and its price, that the customer orders.
     *
     * Toppings is subject to the item category name which is determined by the food name provided by the customer;
     * toppings is only available for Burgers, Dogs, and Sandwiches items.
     *
     * @param   cartForm    details of the food that the customer orders and the order details.
     */
    public void writeToCart(CartForm cartForm) {
        Cart.setTempCart(cartForm);
    }

    /**
     * Return the name of the item category of the food that the customer orders
     *
     * This function returns the name of the item category of the food that the customer orders. The item category name
     * is based on the food item option that the customer selects in the "Take Order" flow.
     *
     * @return the name of the item category of the food that the customer orders
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Return the name of the food that the customer orders
     *
     * This function returns the name of the food that the customer orders.
     *
     * @return the name of the food that the customer orders
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Return the price of the food that the customer orders
     *
     * This function returns the price of the food that the customer orders.
     *
     * @return the price of the food that the customer orders
     */
    public double getFoodPrice() { return foodPrice; }

    /**
     * Return the quantity of the food that the customer requests when he enters his order
     *
     * This function returns the quantity of the food that the customer requests when he enters his order
     *
     * @return the quantity of the food that the customer requests when he enters his order
     */
    public int getReqQuantity() {
        return reqQuantity;
    }

    /**
     * Return the toppings that the customer requests to add
     *
     * This function returns the toppings that the customer requests to add when he orders Burgers item, Dogs item,
     * or Sandwiches item.
     *
     * @return the toppings that the customer requests to add
     */
    public List<HashMap<String, Double>> getToppings() {
        return toppings;
    }
}
