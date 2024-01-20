package component;

public class Food {
    private String foodName;
    private double price;
    private int remainQty;
    private int maxQty;
    private boolean isInStock;

    public Food() {}

    public Food(String foodName, double price, int remainQty, int maxQty, boolean isInStock) {
        this.foodName = foodName;
        this.price = price;
        this.remainQty = remainQty;
        this.maxQty = maxQty;
        this.isInStock = isInStock;
    }

    // debug
    public String toString() {
        return this.foodName + ", " + this.price + ", " + this.remainQty + ", " + this.maxQty + ", " + this.isInStock;
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
