package controller;

import component.CartForm;
import component.Food;
import model.CartModel;
import model.MenuModel;
import view.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerController {
    private MenuModel menuModel;
    private CartModel cartModel;
    private MenuView menuView;
    private ItemView itemView;
    private ErrorView errorView;
    private UpdateResultView updateResultView;
    private OrderResultView orderResultView;
    private ServerErrorView serverErrorView;
    private ToppingsView toppingsView;
    private ToppingSelectionResultView toppingSelectionResultView;
    private CartView cartView;
    private ShutdownView shutdownView;

    public ServerController(MenuModel menuModel, CartModel cartModel) {
        this.menuModel = menuModel;
        this.cartModel = cartModel;
        this.menuView = new MenuView();
        this.itemView = new ItemView();
        this.errorView = new ErrorView();
        this.updateResultView = new UpdateResultView();
        this.orderResultView = new OrderResultView();
        this.serverErrorView = new ServerErrorView();
        this.toppingsView = new ToppingsView();
        this.toppingSelectionResultView = new ToppingSelectionResultView();
        this.cartView = new CartView();
        this.shutdownView = new ShutdownView();
    }

    public Map<String, HashMap<String, List<Food>>> notifyMenuModelToGetsFromDB() {
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

    public HashMap<String, Food> notifyCartModelToUpdateRemainQty(String itemName, String foodName, int reqQty) {
        HashMap<String, Food> dbResponseUpdatedRemainQty = cartModel.sendDBRequestToUpdateRemainQty(
                itemName, foodName, reqQty);
        checkDBResponseToRenderOrderResultView(itemName, foodName, reqQty, dbResponseUpdatedRemainQty);

        if (dbResponseUpdatedRemainQty.size() > 0) {
            return dbResponseUpdatedRemainQty;
        }
        else {
            return new HashMap<>();
        }
//        boolean isSyncWithGlobalRequired = checkDBResponseToRenderOrderResultView(itemName, foodName, reqQty, dbResponseUpdatedRemainQty);
//
//        if (isSyncWithGlobalRequired) {
//            return dbResponseUpdatedRemainQty;
//        }
//        else {
//            return new HashMap<>();
//        }
    }

    private void checkDBResponseToRenderOrderResultView(String itemName, String foodName, int reqQty, HashMap<String, Food> dbResponse) {
        if (itemName != "Toppings") {
            if (dbResponse.size() > 0) {
                //renderOrderResultView().determineViewToShow(itemName, foodName, reqQty);
                renderOrderResultView().printOrderSuccessView(foodName);
            }
            else {
                renderOrderResultView().printInvalidFoodView();
            }
        }
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

    public OrderResultView renderOrderResultView() { return orderResultView; }

    public String renderToppingsView() {
        return toppingsView.printToppingsView();
    }

    public void renderToppingSelectionResultView(List<HashMap<String, Double>> toppingsOrder, List<String> invalidToppingsOrder) {
        toppingSelectionResultView.determineToppingResultView(toppingsOrder, invalidToppingsOrder);
    }

    public void renderCartView(List<CartForm> currentCart) {
        cartView.determineCartView(currentCart);
    }

    public void renderShutDownView() {
        shutdownView.printShutDownView();
    }
}
