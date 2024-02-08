package view;

import component.Food;
import service.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResultView {
    public OrderResultView() {}
// Map<String, HashMap<String, List<Food>>>
    public boolean determineViewToShow(String itemName, String foodName, int reqQty) {
        boolean isSyncWithGlobalRequired = false;

        for (Map.Entry<String, HashMap<String, List<Food>>> entrySet: Global.getMenuHashMap().entrySet()) {
            if (entrySet.getKey() == itemName) {
                for (Map.Entry<String, List<Food>> foodEntry: entrySet.getValue().entrySet()) {
                    String globalMenuHashMapFoodName = foodEntry.getKey();
                    int globalMenuHashMapRemainQty = foodEntry.getValue().get(0).getRemainQty();

                    if (globalMenuHashMapFoodName.equalsIgnoreCase(foodName)) {
                        if (reqQty > globalMenuHashMapRemainQty) {
                            if (globalMenuHashMapRemainQty == 0) {
                                printFoodOutOfStockView();
                            }
                            else if (globalMenuHashMapRemainQty > 0) {
                                printFoodNotEnoughInStockView();
                            }
                        }
                        else {
                            printOrderSuccessView();
                            isSyncWithGlobalRequired = true;
                        }
                    }
                }
            }
        }
        return isSyncWithGlobalRequired;
    }

    private void printOrderSuccessView() { System.out.println("Added to cart."); }

    public void printNoFoodView() { System.out.println("No such food in stock."); }

    private void printFoodOutOfStockView() { System.out.println("The food is out of stock."); }

    private void printFoodNotEnoughInStockView() { System.out.println("Not enough quantity in stock."); }
}
