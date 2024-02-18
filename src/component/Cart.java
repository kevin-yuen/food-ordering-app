package component;

import java.util.*;

public class Cart {
    private static List<CartForm> tempCart = new LinkedList<>();    // hold repeated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private static List<CartForm> cart = new LinkedList<>();        // hold accumulated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private static final double salesTaxRate = 0.089d;   // 4% tax
    private static double subtotal = 0.0d;
    private static double total = 0.0d;

    public Cart() {}

    public void incrementQty() {

    }

    public void decrementQty() {

    }

    public void calculateChangeDue() {

    }

    public void accumulateQuantity(String itemType, CartForm currentItemInTempCart) {
        boolean isTempCartItemInCurrentCart = false;

        System.out.println("TEMP CART:" + currentItemInTempCart);

        if (itemType.equalsIgnoreCase("Fries") ||
                itemType.equalsIgnoreCase("Drinks") ||
                itemType.equalsIgnoreCase("Milkshake Mix-ins")) {
            for (var item : cart) {
                String foodName = currentItemInTempCart.getFoodName();
                double foodPrice = currentItemInTempCart.getFoodPrice();

                if (foodName.equalsIgnoreCase(item.getFoodName())) {
                    int accumulatedQty = currentItemInTempCart.getReqQuantity() + item.getReqQuantity();
                    isTempCartItemInCurrentCart = true;
                    CartForm accumulatedItem = new CartForm(itemType, foodName, foodPrice, accumulatedQty);

                    cart.set(cart.indexOf(item), accumulatedItem);
                    break;
                } else {
                    isTempCartItemInCurrentCart = false;
                }
            }

            if (!isTempCartItemInCurrentCart) cart.add(currentItemInTempCart);
        } else {
            cart.add(currentItemInTempCart);
        }
    }

    private static void calculateSubTotal() {
        for (var item: cart) {
            subtotal += item.getFoodPrice() * item.getReqQuantity();
        }
    }

    public static void calculateTotal() {
        total = (subtotal * salesTaxRate) + subtotal;
    }

    public static void setTempCart(CartForm cartForm) {
        tempCart.add(cartForm);
    }

    public List<CartForm> getTempCart() {
        return tempCart;
    }

    public List<CartForm> getCart() { return cart; }

    public static double getSalesTaxRate() { return salesTaxRate; }

    public static double getSubtotal() {
        calculateSubTotal();
        return subtotal;
    }

    public static double getTotal() {
        calculateTotal();
        return total;
    }

//    public static void setCart(List<CartForm> tempCart) {
//        cart = tempCart;
//    }
}
