package view;

/**
 * This class prints out a result message to the user after DB performs creation or update operation.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

public class UpdateResultView {
    public UpdateResultView() {}

    /**
     * Print out update success message
     *
     * This function prints out success message to the user after an update operation is successfully executed in DB.
     */
    public void printUpdateSuccessView() {
        System.out.println("Food update success.");
    }

    /**
     * Print out no update message
     *
     * This function prints out no update message to the user if DB tries to an update operation on a non-existent
     * food item.
     */
    public void printNoUpdateView() {
        System.out.println("No update has been made.");
    }

    /**
     * Print out added success message
     *
     * This function prints out success message to the user after a new food record is successfully created in DB.
     */
    public void printCreateSuccessView() {
        System.out.println("Food added successfully.");
    }
}
