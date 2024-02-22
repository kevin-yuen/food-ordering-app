package component;

/**
 * This class contains all the ordered food items. It is responsible for consolidating all the food items that the
 * customer requests for in a single order and calculating subtotal and total amount of the order.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import java.util.*;

public class Cart {
    private static List<CartForm> tempCart = new LinkedList<>();    // contain repeated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private List<CartForm> cart = new LinkedList<>();        // contain accumulated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private final double salesTaxRate = 0.089d;   // 4% tax
    private double subtotal = 0.0d;
    private double total = 0.0d;

    public Cart() {}

    /**
     * This function adds the food to Cart by accumulating the order quantity by food name if the ordered item is
     * Fries, Drinks, or Milkshake Mix-ins. If the ordered item is Burgers, Dogs, or Sandwiches, each item gets added
     * to cart individually instead (i.e. the order quantity does not get accumulated).
     *
     * @param itemType                name of the item category where the food is being ordered
     * @param currentItemInTempCart   food item placed in temporary cart
     */
    public void accumulateQuantity(String itemType, CartForm currentItemInTempCart) {
        boolean isTempCartItemInCurrentCart = false;

        if (itemType.equalsIgnoreCase("Fries") ||
                itemType.equalsIgnoreCase("Drinks") ||
                itemType.equalsIgnoreCase("Milkshake Mix-ins")) {
            for (var item : this.cart) {
                String foodName = currentItemInTempCart.getFoodName();
                double foodPrice = currentItemInTempCart.getFoodPrice();

                if (foodName.equalsIgnoreCase(item.getFoodName())) {
                    int accumulatedQty = currentItemInTempCart.getReqQuantity() + item.getReqQuantity();
                    isTempCartItemInCurrentCart = true;
                    CartForm accumulatedItem = new CartForm(itemType, foodName, foodPrice, accumulatedQty);

                    this.cart.set(this.cart.indexOf(item), accumulatedItem);
                    break;
                }
            }

            if (!isTempCartItemInCurrentCart) this.cart.add(currentItemInTempCart);
        } else {
            this.cart.add(currentItemInTempCart);
        }
    }

    /**
     * This function accumulates the amount of each ordered food item from cart. The amount does NOT include sales
     * tax amount.
     */
    private void calculateSubTotal() {
        double toppingPrice = 0.0d;

        for (var item : this.cart) {
            List<HashMap<String, Double>> topping = item.getToppings();

            if (topping.size() > 0) {
                for (var tElement : topping) {
                    for (Map.Entry<String, Double> tDets : tElement.entrySet()) {
                        toppingPrice += tDets.getValue();
                    }
                }
            }
            this.subtotal += item.getFoodPrice() * item.getReqQuantity();
        }
        this.subtotal += toppingPrice;
    }

    /**
     * This function accumulates the amount of each ordered food item from cart (i.e. subtotal amount) and sales tax
     * amount.
     */
    public void calculateTotal() {
        this.total = (this.subtotal * this.salesTaxRate) + this.subtotal;
    }

    /**
     * This function removes all the food items from temporary cart once all items are added to Cart.
     */
    public void clearTempCart() {
        tempCart.clear();
    }

    /**
     * This function removes all food items from Cart once payment is complete.
     */
    public void clearCart() {
        this.cart.clear();
    }

    /**
     * This function adds the CartForm object to temporary cart every time when the customer makes an order.
     * The CartForm object contains details of the food, such as food name, price, item category name, order quantity,
     * and/or topping names, that the customer orders.
     *
     * @param cartForm  food name, price, item category name, order quantity, and/or topping name(s) and price
     */
    public static void setTempCart(CartForm cartForm) {
        tempCart.add(cartForm);
    }

    /**
     * This function returns the temporary cart, which contains the initial order details of each food item that the
     * customer orders.
     *
     * @return the temporary cart
     */
    public List<CartForm> getTempCart() {
        return tempCart;
    }

    /**
     * This function returns the cart, which contains the accumulated quantity of each food item that the customer orders.
     *
     * @return cart
     */
    public List<CartForm> getCart() { return cart; }

    /**
     * This function returns the default sales tax rate, which will be used to sum up subtotal amount of the order.
     *
     * @return sales tax rate
     */
    public double getSalesTaxRate() { return salesTaxRate; }

    /**
     * This function returns the subtotal amount prior to sales tax amount.
     *
     * @return subtotal amount
     */
    public double getSubtotal() {
        this.calculateSubTotal();
        return subtotal;
    }

    /**
     * This function returns the total amount, which is the sum of the subtotal amount and sales tax amount.
     *
     * @return total amount
     */
    public double getTotal() {
        this.calculateTotal();
        return total;
    }

    /**
     * This function sets the cart to temporary cart. It is only called when the temporary cart contains only one food item.
     *
     * @param tempCart  temporary cart that contains only one food item
     */
    public void setCart(List<CartForm> tempCart) {
        this.cart.add(tempCart.get(0));
    }
}
