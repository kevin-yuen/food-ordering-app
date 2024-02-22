package view;

/**
 * This class prints out a result message to the user after DB performs create or update operation.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

public class UpdateResultView {
    public UpdateResultView() {}

    /**
     * This function prints out success message to the user after an update operation is successfully executed in DB.
     */
    public void printUpdateSuccessView() {
        System.out.println("Food update success.");
    }

    /**
     * This function prints out no update message to the user if DB tries to perform an update operation on a
     * non-existent food item.
     */
    public void printNoUpdateView() {
        System.out.println("No update has been made.");
    }

    /**
     * This function prints out success message to the user after a new food record is successfully created in DB.
     */
    public void printCreateSuccessView() {
        System.out.println("Food added successfully.");
    }
}
