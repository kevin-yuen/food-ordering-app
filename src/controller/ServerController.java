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
     * @return The list of food details of each food per MenuModel
     */
    public Map<String, HashMap<String, List<Food>>> notifyMenuModelToGetFromDB() {
        return menuModel.getLatestMenuItemsFromDB();
    }

    /**
     * Forward price update request to MenuModel
     *
     * This function forwards the system request of updating food price to MenuModel and renders UpdateResult view
     * based on the DB response.
     *
     * @param   itemName        the item category of the food which its price is requested to be updated
     * @param   foodName        the name of the food which its price is requested to be updated
     * @param   requestedPrice  the price to be updated to
     * @return                  possible returns:
     *                              the new food price along with its other food details
     *                              empty HashMap object
     */
    public HashMap<String, Food> notifyMenuModelToUpdatePrice(String itemName, String foodName,
                                                                         double requestedPrice) {
        HashMap<String, Food> dbResponse = menuModel.sendDBRequestToUpdateFoodDets(itemName, foodName,
                requestedPrice, null);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    /**
     * Redirect the user request of creating a new food to MenuModel
     *
     * This function redirects the user request of creating a new food to MenuModel and renders UpdateResult view
     * based on the DB response.
     *
     * @param   itemName    the name of the item category based on user's selected item category where he wants to create a new food under
     * @param   foodName    the name of the new food to be recorded in the DB
     * @param   price       the price of the new food to be recorded in the DB
     * @param   maxQty      the max. quantity of the new food to be recorded in the DB
     * @return              possible returns:
     *                          the details of the new food that is being created along with its item category id
     *                          empty HashMap object
     */
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

    /**
     * Redirect the user request of updating max. quantity of the food to MenuModel
     *
     * This function redirects the user request of updating max. quantity of the food to MenuModel and renders
     * UpdateResult view based on the DB response.
     *
     * @param   itemName        the name of the item category based on user's selected item category
     * @param   foodName        the name of the food which its max. quantity needs to be updated
     * @param   requestedMaxQty the max. quantity that user provides and to be updated to
     * @return                  possible returns:
     *                              the new max. quantity and updated remaining quantity along with its other food details
     *                              empty HashMap object
     */
    public HashMap<String, Food> notifyMenuModelToUpdateMaxQty(String itemName, String foodName,
                                                                          int requestedMaxQty) {
        HashMap<String, Food> dbResponse = menuModel.sendDBRequestToUpdateFoodDets(
                itemName, foodName, null, requestedMaxQty);

        checkDBResponseToRenderResultView(dbResponse);
        return dbResponse;
    }

    /**
     * Redirect the customer's order request to CartModel
     *
     * This function redirects the customer's food order request to CartModel.
     *
     * @param   itemName    name of the item category where the food is being ordered by the customer
     * @param   foodName    name of the food that the customer orders
     * @param   reqQty      order quantity that customer requests for
     * @return              possible returns:
     *                          the latest remaining quantity along with food name, price, max. quantity, and item category name
     *                          empty HashMap object
     */
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

    /**
     * Render OrderResult view
     *
     * This function renders OrderResult view to print out a message to the customer as the result of food order
     * request of non-Toppings items.
     *
     * @param   itemName    name of the item category where the food is being ordered by the customer
     * @param   foodName    name of the food that the customer orders
     * @param   dbResponse  the latest remaining quantity of the food along with item name, food name, price, and
     *                      max. quantity
     */
    private void checkDBResponseToRenderOrderResultView(String itemName, String foodName, HashMap<String, Food> dbResponse) {
        if (itemName != "Toppings") {
            if (dbResponse.size() > 0) {
                renderOrderResultView().printOrderSuccessView(foodName);
            } else {
                renderOrderResultView().printInvalidFoodView();
            }
        }
    }

    /**
     * Render UpdateResult view to show the result of the update on food price and max. quantity
     *
     * This function renders UpdateResult view to show either success message or no update message
     *
     * @param   dbResponse  the latest food details of the food from DB
     */
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

    /**
     * Render Menu view
     *
     * This function renders menu view to print out the latest food details as a menu
     *
     * @param   foodHashMap     the global variable, menuHashMap, which contains the latest food details
     */
    public void renderMenuView(Map<String, HashMap<String, List<Food>>> foodHashMap) {
        menuView.printMenuView(foodHashMap);
    }

    private UpdateResultView renderUpdateResultView() {
        return updateResultView;
    }

    /**
     * Render Order view
     *
     * This function renders OrderResult view to print out a message to the customer as the result of food order request.
     *
     * @return  OrderResult view
     */
    public OrderResultView renderOrderResultView() { return orderResultView; }

    /**
     * Render Toppings view
     *
     * This function renders Toppings view to prepare the list of available topping names.
     *
     * @return  the list of available toppings
     */
    public String renderToppingsView() {
        return toppingsView.printToppingsView();
    }

    public void renderToppingSelectionResultView(List<HashMap<String, Double>> toppingsOrder, List<String> invalidToppingsOrder) {
        toppingSelectionResultView.determineToppingResultView(toppingsOrder, invalidToppingsOrder);
    }

    /**
     * Render Cart view
     *
     * This function renders Cart view to determine the final cart view to display to the customer.
     */
    public void renderCartView(List<CartForm> currentCart, Cart cart) {
        cartView.determineCartView(currentCart, cart);
    }

    /**
     * Render Payment view
     *
     * This function renders Payment view to determine the final payment view to display to the customer.
     */
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
