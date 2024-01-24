package controller;

import component.Food;
import model.MenuModel;
import view.ErrorView;
import view.ItemView;
import view.MenuView;
import view.UpdateResultView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerController {
    private MenuModel menuModel;
    private MenuView menuView;
    private ItemView itemView;
    private ErrorView errorView;
    private UpdateResultView updateResultView;

    public ServerController(MenuModel menuModel, MenuView menuView) {
        this.menuModel = menuModel;
        this.menuView = menuView;
        this.itemView = new ItemView();
        this.errorView = new ErrorView();
        this.updateResultView = new UpdateResultView();
    }

    public HashMap<String, ArrayList<ArrayList<Food>>> notifyMenuModelToGetsFromDB() {
        return menuModel.getLatestMenuItemsFromDB();
    }

    public void notifyMenuModelToComposeLatestMenu() {}

    public void notifyMenuModelToCreateFood() {}

    public HashMap<String, ArrayList<Food>> notifyMenuModelToUpdatePrice(String itemName, String foodName, double price) {
        HashMap<String, ArrayList<Food>> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(itemName, foodName, price,
                null);

        if (dbResponse.size() > 0) {
            updateResultView.printUpdateSuccessView();
        }
        else {
            updateResultView.printNoFoodFoundView();
        }
        return dbResponse;  // {itemName=[foodName, price, remainQty, maxQty]}
    }

    public HashMap<String, ArrayList<Food>> notifyMenuModelToUpdateMaxQty(String itemName, String foodName, int maxQty) {
        HashMap<String, ArrayList<Food>> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(
                itemName, foodName, null, maxQty);

        if (dbResponse.size() > 0) {
            updateResultView.printUpdateSuccessView();
        }
        else {
            updateResultView.printNoFoodFoundView();
        }
        return dbResponse;  // {itemName=[foodName, price, remainQty, maxQty]}
    }

    public String renderMainView(int appState, HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap) {
        if (appState == 1) {
            return renderItemView(menuHashMap);
        }
        else if (appState == 2) {
            // render Menu view
        }
        return null;
    }

    public void renderErrorView() {
        errorView.printErrorView();
    }

    public String renderItemView(HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap) {
        return itemView.printItemView(menuHashMap);
    }

    public void renderMenuView(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap) {
        menuView.printMenuView(foodHashMap);
    }
}
