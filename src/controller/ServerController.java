package controller;

import component.Food;
import model.MenuModel;
import view.ErrorView;
import view.ItemView;
import view.MenuView;
import view.UpdateResultView;

import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<String, ArrayList<Food>> notifyMenuModelToUpdatePrice(String itemName, String foodName,
                                                                         double requestedPrice) {
        HashMap<String, ArrayList<Food>> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(itemName, foodName,
                requestedPrice, null);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    public HashMap<String, ArrayList<Food>> notifyMenuModelToCreateFood(String itemName, String foodName,
                                                                        double price, int maxQty) {
        HashMap<String, ArrayList<Food>> dbResponse = menuModel.sendDBRequestToCreateNewFood(itemName, foodName, price,
                maxQty);

        if (dbResponse.size() > 0) {
            renderUpdateResultView().printCreateSuccessView();
        }
        else {
            renderUpdateResultView().printNoUpdateView();
        }
        return dbResponse;
    }

    public HashMap<String, ArrayList<Food>> notifyMenuModelToUpdateMaxQty(String itemName, String foodName,
                                                                          int requestedMaxQty) {
        HashMap<String, ArrayList<Food>> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(
                itemName, foodName, null, requestedMaxQty);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    private void checkDBResponseToRenderResultView(HashMap<String, ArrayList<Food>> dbResponse) {
        if (dbResponse.size() > 0) {
            renderUpdateResultView().printUpdateSuccessView();
        }
        else {
            renderUpdateResultView().printNoUpdateView();
        }
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

    private String renderItemView(HashMap<String, ArrayList<ArrayList<Food>>> menuHashMap) {
        return itemView.printItemView(menuHashMap);
    }

    public void renderMenuView(HashMap<String, ArrayList<ArrayList<Food>>> foodHashMap) {
        menuView.printMenuView(foodHashMap);
    }

    private UpdateResultView renderUpdateResultView() {
        return updateResultView;
    }
}
