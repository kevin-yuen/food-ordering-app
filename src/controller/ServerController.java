package controller;

/**
 * This class handles user requests by forwarding them to Model layer for data retrieval,
 * redirecting db responses to View layer for presentation, and sending back presentation outputs to user.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Cart;
import component.CartForm;
import component.Customer;
import component.Food;
import model.CartModel;
import model.MenuModel;
import view.*;

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
    private PaymentView paymentView;
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
        this.paymentView = new PaymentView();
        this.shutdownView = new ShutdownView();
    }

    /**
     * Redirect the system request to MenuModel
     *
     * This function redirects the system request of retrieving the latest menu items to MenuModel
     *
     * @return      The list of food details of each food per MenuModel
     */
    public Map<String, HashMap<String, List<Food>>> notifyMenuModelToGetFromDB() {
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
        } else {
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
        checkDBResponseToRenderOrderResultView(itemName, foodName, dbResponseUpdatedRemainQty);

        if (dbResponseUpdatedRemainQty.size() > 0) {
            return dbResponseUpdatedRemainQty;
        } else {
            return new HashMap<>();
        }
    }

    private void checkDBResponseToRenderOrderResultView(String itemName, String foodName, HashMap<String, Food> dbResponse) {
        if (itemName != "Toppings") {
            if (dbResponse.size() > 0) {
                renderOrderResultView().printOrderSuccessView(foodName);
            } else {
                renderOrderResultView().printInvalidFoodView();
            }
        }
    }

    private void checkDBResponseToRenderResultView(HashMap<String, Food> dbResponse) {
        if (dbResponse.size() > 0) {
            renderUpdateResultView().printUpdateSuccessView();
        } else {
            renderUpdateResultView().printNoUpdateView();
        }
    }

    /**
     * Redirect the system to ErrorView
     *
     * This function redirects system to ErrorView if user chooses an invalid operation option
     */
    public void renderErrorView() {
        errorView.printErrorView();
    }

    /**
     * Redirect the system request to ServerErrorView
     *
     * This function redirects system request to ServerErrorView when the system fails to retrieve data from DB for the
     * first time.
     */
    public void renderServerErrorView() {
        serverErrorView.printServerErrorView();
    }

    /**
     * Redirect the system request to ItemView
     *
     * This function redirects to ItemView after the system successfully retrieves data from DB for the first time.
     *
     * @param   appState        current app status, which is determined by the selected operation option
     * @param   menuHashMap     the latest food item list
     * @return                  The entire message that asks user to select a food item
     */
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

    public void renderCartView(List<CartForm> currentCart, Cart cart) {
        cartView.determineCartView(currentCart, cart);
    }

    public boolean renderPaymentView(Cart cart, Customer customer) {
        return paymentView.printPaymentView(cart, customer);
    }

    /**
     * Redirect the system to ShutDownView
     *
     * This function redirects to ShutDownView when appState becomes 4 (i.e. user chooses to shut down the system).
     */
    public void renderShutDownView() {
        shutdownView.printShutDownView();
    }
}
