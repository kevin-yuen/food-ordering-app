package component;

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

    public void writeToCart(CartForm cartForm) {
        Cart.setTempCart(cartForm);
    }

    public String getItemType() {
        return itemType;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodPrice() { return foodPrice; }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public List<HashMap<String, Double>> getToppings() {
        return toppings;
    }
}
