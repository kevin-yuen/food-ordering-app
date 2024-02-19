package view;

public class OrderResultView {
    public OrderResultView() {}

    public void printOrderSuccessView(String foodName) { System.out.printf("%s added to cart.\n", foodName); }

    public void printInvalidFoodView() { System.out.println("No such food in stock."); }

    public void printOutOfStockView() { System.out.println("The food is temporarily out of stock."); }

    public void printInsufficientQuantityView() { System.out.println("Not sufficient quantity in stock."); }
}
