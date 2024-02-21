package view;

/**
 * This class prints out a message to the customer as the result of food order request. The messages in this class
 * are only applicable to non-Toppings items.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

public class OrderResultView {
    public OrderResultView() {}

    /**
     * Print out order success message
     *
     * This function prints out order success message if the food is successfully added to cart.
     *
     * @param   foodName    name of the food that the customer enters
     */
    public void printOrderSuccessView(String foodName) { System.out.printf("%s added to cart.\n", foodName); }

    /**
     * Print out invalid food message
     *
     * This function prints out invalid food message if there is no such food under the specified item category.
     *
     */
    public void printInvalidFoodView() { System.out.println("No such food in stock."); }

    /**
     * Print out out of stock message
     *
     * This function prints out out of stock message if the current remaining quantity of the food that the customer
     * wishes to order is 0.
     *
     */
    public void printOutOfStockView() { System.out.println("The food is temporarily out of stock."); }

    /**
     * Print out insufficient quantity message
     *
     * This function prints out insufficient quantity message if the current remaining quantity is less than
     * the quantity that the customer orders.
     *
     */
    public void printInsufficientQuantityView() { System.out.println("Not sufficient quantity in stock."); }
}
