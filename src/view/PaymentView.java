package view;

/**
 * This class is responsible for printing out payment view to the customer.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Cart;
import component.Customer;

import java.text.DecimalFormat;

public class PaymentView {
    public PaymentView() {}

    /**
     * Print out payment view to the customer.
     *
     * This function verifies the payment input against the total amount and prints out the corresponding payment view
     * to the customer.
     *
     * @param   cart        cart object
     * @param   customer    customer object
     * @return              always True as long as the payment input is valid (i.e. the payment amount is greater than
     *                      or same as the total amount)
     */
    public boolean printPaymentView(Cart cart, Customer customer) {
        boolean isPaymentValid = false;
        do {
            System.out.print("Enter payment for your order: ");

            try {
                final DecimalFormat df = new DecimalFormat("0.00");
                double paymentInput = Double.parseDouble(customer.makePayment());
                double total = Double.parseDouble(df.format(cart.getTotal()));
                double changeDue = 0.0d;

                if (paymentInput >= total) {
                    if (paymentInput > total) changeDue = paymentInput - total;

                    System.out.printf(String.format("%s: $%.2f\n", "Change Due", changeDue));
                    System.out.println("Thank you. Have a good day!");
                    isPaymentValid = true;
                } else {
                    System.out.println("Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Your payment is not valid. Please try again.");
            }
        } while (!isPaymentValid);
        return true;
    }
}
