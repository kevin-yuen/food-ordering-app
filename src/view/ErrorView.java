package view;

/**
 * This class is responsible for printing out error message.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

public class ErrorView {
    public ErrorView() {}

    /**
     * This function prints out error message which notifies user the error that is being encountered. It
     * occurs when user selects an invalid option on any system menu view.
     */
    public void printErrorView() {
        System.out.println("Invalid selection. Please try again.");
    }
}
