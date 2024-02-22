package view;

/**
 * This class is responsible for printing out server error view to the customer.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

public class ServerErrorView {
    public ServerErrorView() {}

    /**
     * This function prints out server error view if the system fails to retrieve data from DB for the first time. The
     * program ends automatically if the situation occurs.
     */
    public void printServerErrorView() {
        System.out.println("Server Error. Please try again later.\nExiting the program...");
        System.exit(0);
    }
}
