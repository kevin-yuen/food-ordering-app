package view;

public class UpdateResultView {
    public UpdateResultView() {}

    public void printUpdateSuccessView() {
        System.out.println("Food update success.");
    }

    public void printNoUpdateView() {
        System.out.println("No update has been made.");
    }

    public void printCreateSuccessView() {
        System.out.println("Food added successfully.");
    }

    public void printNoFoodView() { System.out.println("No such food in stock."); }

    public void printFoodOutOfStockView() { System.out.println("The food is out of stock."); }
}
