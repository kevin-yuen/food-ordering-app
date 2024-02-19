package component;

import java.util.*;

public class Cart {
    private static List<CartForm> tempCart = new LinkedList<>();    // hold repeated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private List<CartForm> cart = new LinkedList<>();        // hold accumulated Fries, Drinks, and Milkshake mix-ins items in addition to Burgers, Dogs, and Sandwiches items
    private final double salesTaxRate = 0.089d;   // 4% tax
    private double subtotal = 0.0d;
    private double total = 0.0d;

    public Cart() {}

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

    public void calculateTotal() {
        this.total = (this.subtotal * this.salesTaxRate) + this.subtotal;
    }

    public void clearTempCart() {
        tempCart.clear();
    }

    public void clearCart() {
        this.cart.clear();
    }

    public static void setTempCart(CartForm cartForm) {
        tempCart.add(cartForm);
    }

    public List<CartForm> getTempCart() {
        return tempCart;
    }

    public List<CartForm> getCart() { return cart; }

    public double getSalesTaxRate() { return salesTaxRate; }

    public double getSubtotal() {
        this.calculateSubTotal();
        return subtotal;
    }

    public double getTotal() {
        this.calculateTotal();
        return total;
    }

    public void setCart(List<CartForm> tempCart) {
        this.cart.add(tempCart.get(0));
    }
}
