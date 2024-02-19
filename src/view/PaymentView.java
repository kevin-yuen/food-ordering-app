package view;

import component.Cart;
import component.Customer;

import java.text.DecimalFormat;

public class PaymentView {
    public PaymentView() {}

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
