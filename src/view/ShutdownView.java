package view;

/**
 * This class is responsible for printing out the "shut down" message.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */
public class ShutdownView {
    public ShutdownView() {}

    /**
     * This function prints out the "shut down" message which notifies user that the system is getting terminated. It
     * occurs when user selects the fourth option to terminate the system on the Operations menu.
     */
    public void printShutDownView() {
        System.out.println("Shutting down...");
    }
}
