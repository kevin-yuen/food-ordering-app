package controller;

import component.Food;
import model.MenuModel;
import view.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerController {
    private MenuModel menuModel;
    private MenuView menuView;
    private ItemView itemView;
    private ErrorView errorView;
    private UpdateResultView updateResultView;
    private OrderResultView orderResultView;
    private ServerErrorView serverErrorView;

    public ServerController(MenuModel menuModel, MenuView menuView) {
        this.menuModel = menuModel;
        this.menuView = menuView;
        this.itemView = new ItemView();
        this.errorView = new ErrorView();
        this.updateResultView = new UpdateResultView();
        this.orderResultView = new OrderResultView();
        this.serverErrorView = new ServerErrorView();
    }

    public Map<String, HashMap<String, List<Food>>>  notifyMenuModelToGetsFromDB() {
        return menuModel.getLatestMenuItemsFromDB();
    }

    public HashMap<String, Food> notifyMenuModelToUpdatePrice(String itemName, String foodName,
                                                                         double requestedPrice) {
        HashMap<String, Food> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(itemName, foodName,
                requestedPrice, null);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    public HashMap<String, Food> notifyMenuModelToCreateFood(String itemName, String foodName,
                                                                        double price, int maxQty) {
        HashMap<String, Food> dbResponse = menuModel.sendDBRequestToCreateNewFood(itemName, foodName, price,
                maxQty);

        if (dbResponse.size() > 0) {
            renderUpdateResultView().printCreateSuccessView();
        }
        else {
            renderUpdateResultView().printNoUpdateView();
        }
        return dbResponse;
    }

    public HashMap<String, Food> notifyMenuModelToUpdateMaxQty(String itemName, String foodName,
                                                                          int requestedMaxQty) {
        HashMap<String, Food> dbResponse = menuModel.sendDBRequestToUpdateFoodDetails(
                itemName, foodName, null, requestedMaxQty);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    public HashMap<String, Food> notifyMenuModelToUpdateRemainQty(String itemName, String foodName, int reqQty) {
        HashMap<String, Food> dbResponseUpdatedRemainQty = menuModel.sendDBRequestToUpdateRemainQty(
                itemName, foodName, reqQty);
        boolean isSyncWithGlobalRequired = checkDBResponseToRenderOrderResultView(itemName, foodName, reqQty, dbResponseUpdatedRemainQty);

        if (isSyncWithGlobalRequired) {
            return dbResponseUpdatedRemainQty;
        }
        else {
            return new HashMap<>();
        }
    }

    private boolean checkDBResponseToRenderOrderResultView(String itemName, String foodName, int reqQty, HashMap<String, Food> dbResponse) {
        boolean isSyncWithGlobalRequired = false;

        if (dbResponse.size() > 0) {
            isSyncWithGlobalRequired = renderOrderResultView().determineViewToShow(itemName, foodName, reqQty);
        }
        else {
            renderOrderResultView().printNoFoodView();
        }
        return isSyncWithGlobalRequired;
    }

    private void checkDBResponseToRenderResultView(HashMap<String, Food> dbResponse) {
        if (dbResponse.size() > 0) {
            renderUpdateResultView().printUpdateSuccessView();
        }
        else {
            renderUpdateResultView().printNoUpdateView();
        }
    }

    public void renderErrorView() {
        errorView.printErrorView();
    }

    public void renderServerErrorView() {
        serverErrorView.printServerErrorView();
    }

    public String renderItemView(int appState, Map<String, HashMap<String, List<Food>>> menuHashMap) {
        return itemView.printItemView(appState, menuHashMap);
    }

    public void renderMenuView(Map<String, HashMap<String, List<Food>>> foodHashMap) {
        menuView.printMenuView(foodHashMap);
    }

    private UpdateResultView renderUpdateResultView() {
        return updateResultView;
    }

    private OrderResultView renderOrderResultView() { return orderResultView; }
}
