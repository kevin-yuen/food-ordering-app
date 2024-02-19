package component;

public class Food {
    private String itemName = null;
    private String foodName = null;
    private double price = 0.0d;
    private int remainQty = 0;
    private int maxQty = 0;

    public Food() {}

    public Food(String itemType, String foodName, double price, int remainQty, int maxQty) {
        this.itemName = itemType;
        this.foodName = foodName;
        this.price = price;
        this.remainQty = remainQty;
        this.maxQty = maxQty;
    }

    public Food(String foodName, double price, int remainQty, int maxQty) {
        this.foodName = foodName;
        this.price = price;
        this.remainQty = remainQty;
        this.maxQty = maxQty;
    }

    public Food(double price, int remainQty, int maxQty) {
        this.price = price;
        this.remainQty = remainQty;
        this.maxQty = maxQty;
    }

    public String toString() {
        if (this.itemName == null && this.foodName == null) {
            return this.price + ", " + this.remainQty + ", " + this.maxQty;
        } else if (this.itemName == null) {
            return this.foodName + ", " + this.price + ", " + this.remainQty + ", " + this.maxQty;
        } else {
            return this.itemName + ", " + this.foodName + ", " + this.price + ", " + this.remainQty + ", " + this.maxQty;
        }
    }

    public String getItemName() {
        return itemName;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    public int getRemainQty() {
        return remainQty;
    }

    public int getMaxQty() {
        return maxQty;
    }
}
