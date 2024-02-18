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

//    public void determineViewToShow(String itemName, String foodName, int reqQty) {
//        //boolean isSyncWithGlobalRequired = false;
//
//        for (Map.Entry<String, HashMap<String, List<Food>>> entrySet: Global.getMenuHashMap().entrySet()) {
//            if (entrySet.getKey().equalsIgnoreCase(itemName)) {
//                for (Map.Entry<String, List<Food>> foodEntry: entrySet.getValue().entrySet()) {
//                    String globalMenuHashMapFoodName = foodEntry.getKey();
//                    int globalMenuHashMapRemainQty = foodEntry.getValue().get(0).getRemainQty();
//
//                    if (globalMenuHashMapFoodName.equalsIgnoreCase(foodName)) {
//                        if (reqQty > globalMenuHashMapRemainQty) {
//                            if (globalMenuHashMapRemainQty == 0) {
//                                printOutOfStockView();
//                            }
//                            else if (globalMenuHashMapRemainQty > 0) {
//                                printInsufficientQuantityView();
//                            }
//                        }
//                        else {
//                            printOrderSuccessView(foodName);
//                            //isSyncWithGlobalRequired = true;
//                        }
//                    }
//                }
//            }
//        }
//        //return isSyncWithGlobalRequired;
//    }

    public void printOrderSuccessView(String foodName) { System.out.printf("%s added to cart.\n", foodName); }

    public void printInvalidFoodView() { System.out.println("No such food in stock."); }

    public void printOutOfStockView() { System.out.println("The food is temporarily out of stock."); }

    public void printInsufficientQuantityView() { System.out.println("Not sufficient quantity in stock."); }
}
