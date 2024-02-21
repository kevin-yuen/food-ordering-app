package view;

/**
 * This class is responsible for creating and formatting the cart view for the customer.
 *
 * @author Kevin Yuen
 * @lastUpdatedDate 2/19/2024
 */

import component.Cart;
import component.CartForm;

import java.text.SimpleDateFormat;
import java.util.*;

public class CartView {
    private final String storeNo = "TX-30970Z";
    private final String storeName = "FIVE GUYS";
    private final String storeAddress1 = "1234 Sugarloaf Pkwy";
    private final String storeAddress2 = "Suwanne GA, 87321";
    private final String storePhoneNo = "(678)-999-9999";
    private final int borderLen = 150;
    private final String subtotalLabel = "Sub Total", salesTaxLabel = "Sales Tax", totalLabel = "Order Total";

    public CartView() {}

    /**
     * Determine the final cart view to display to the customer
     *
     * This function determines which cart view to display to the customer.
     *
     * @param   currentCart     hold the accumulated quantity of each food item that the customer orders.
     * @param   cart            cart object
     */
    public void determineCartView(List<CartForm> currentCart, Cart cart) {
        if (currentCart.size() == 0) {
            printNoItemInCartView();
        } else {
            printCartView(currentCart, cart);
        }
    }

    /**
     * Print out no item view
     *
     * This function prints out no item view if no item is found in cart.
     *
     */
    private void printNoItemInCartView() {
        System.out.println("No item in cart.");
    }

    /**
     * Format the cart
     *
     * This function formats the line that has only one content in the cart view.
     *
     * @param   content     detail of the store
     * @return              the entire line that contains only one content
     */
    private String generateOneLabelLine(String content) {
        int numOfSpRemain = borderLen - content.length() - 2;   // subtract 2 due to * on both sides
        int numOfSpBeg = numOfSpRemain / 2, numOfSpEnd = numOfSpRemain - numOfSpBeg;

        return "*".concat(" ".repeat(numOfSpBeg)).concat(content).concat(" ".repeat(numOfSpEnd)).concat("*\n");
    }

    /**
     * Format the cart
     *
     * This function formats the line that has 2 contents in the cart view.
     *
     * @param   content1            the content on the left-hand side of the cart view
     * @param   content2            the content on the right-hand side of the cart view
     * @param   numOfSpToFillBeg    the number of spaces to fill in prior to the content on the left-hand side of the cart view
     * @param   numOfSpToFillEnd    the number of spaces to fill in after the content on the right-hand side of the cart view
     * @return                      the entire line that contains 2 contents
     */
    private String generateTwoLabelLine(String content1, String content2, int numOfSpToFillBeg, int numOfSpToFillEnd) {
        int numOfSpRemain = borderLen - (content1.length() + content2.length()) - 2;    // subtract 2 due to * on both sides
        int numOfSpToFillBtw = numOfSpRemain - numOfSpToFillBeg - numOfSpToFillEnd;

        return "*".concat(" ".repeat(numOfSpToFillBeg)).concat(content1).concat(" ".repeat(numOfSpToFillBtw)).concat(content2).concat(" ".repeat(numOfSpToFillEnd)).concat("*\n");
    }

    /**
     * Generate header section of the cart view
     *
     * This function generates header section of the cart view.  The header section includes:
     *  - store number
     *  - store address
     *  - phone number of the store
     *  - current date and time of when the cart view is generated
     *  - store name
     *
     * @param   cartBorder  top border
     * @return              the formatted header section
     */
    private String generateHeaderSection(String cartBorder) {
        String header = "";
        int cartBorderLen = cartBorder.length()-1;  // due to "\n" from user input, recalculate cartBorder length

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"),
                timeFormat = new SimpleDateFormat("h:mm a");
        String currentDt = dateFormat.format(currentDate), currentTime = timeFormat.format(currentDate);

        for (int headerLCtner = 0; headerLCtner < 10; headerLCtner++) {
            if (headerLCtner == 0) {
                header += "*".repeat(cartBorderLen);
            } else if (headerLCtner == 1 || headerLCtner == 2 || headerLCtner == 7) {
                header += "*".concat(" ".repeat(148)).concat("*\n");    // empty line
            } else {
                header += switch (headerLCtner) {
                    case 3 -> generateOneLabelLine(String.format("%s %s", "STORE # ", storeNo));
                    case 4 -> generateOneLabelLine(storeAddress1);
                    case 5 -> generateOneLabelLine(storeAddress2);
                    case 6 -> generateOneLabelLine(String.format("%s %s", "Phone: ", storePhoneNo));
                    case 8 -> generateTwoLabelLine(currentDt, currentTime, 10, 10);
                    case 9 -> generateOneLabelLine(storeName);
                    default -> "";
                };
            }
        }
        return header;
    }

    /**
     * Generate body section of the cart view
     *
     * This function generates body section of the cart view.  The body section includes:
     *  - accumulated quantity of Fries, Drinks, and/or Milkshake Mix-ins item
     *  - default quantity (i.e. 1) of Burgers, Dogs, and/or Sandwiches item
     *  - food name
     *  - default food price
     *  - total price per total quantity of each food item
     *  - subtotal amount
     *  - sales tax amount
     *  - total amount
     *
     * @param   currentCart     hold the accumulated quantity of each food item that the customer orders.
     * @return                  the formatted body section
     */
    private String generateBodySection(List<CartForm> currentCart) {
        String body = "";

        for (var item : currentCart) {
            int totalQty = item.getReqQuantity();
            double priceForOne = item.getFoodPrice();
            String foodName = item.getFoodName(), pricePerTotalQty = String.format("$%.2f", priceForOne * totalQty);

            String totalQtyAndName = String.format("%s %s", totalQty, foodName);

            if (item.getItemType().equalsIgnoreCase("Burgers") ||
                    item.getItemType().equalsIgnoreCase("Dogs") ||
                    item.getItemType().equalsIgnoreCase("Sandwiches")) {
                String toppingDets = "";
                List<HashMap<String, Double>> toppings = item.getToppings();

                if (toppings.size() > 0) {
                    for (var topping : toppings) {
                        for (Map.Entry<String, Double> tEntry: topping.entrySet()) {
                            String toppingName = tEntry.getKey();
                            String toppingPrice = String.format("$%.2f", tEntry.getValue());

                            toppingDets += generateTwoLabelLine(toppingName, toppingPrice, 40, 35);
                        }
                    }
                }

                body += generateTwoLabelLine(totalQtyAndName, pricePerTotalQty, 35, 35);
                if (toppingDets != "") body += toppingDets;
            } else {
                String fmtPriceForOne = String.format("(1 @ $%.2f)", priceForOne);
                String totalQtyNameAndPriceForOne = String.format("%s %s", totalQtyAndName, fmtPriceForOne);

                body += generateTwoLabelLine(totalQtyNameAndPriceForOne, pricePerTotalQty, 35, 35);
            }
        }
        return body;
    }

    /**
     * Generate footer section of the cart view
     *
     * This function generates footer section of the cart view.  The footer section includes:
     *  - subtotal amount
     *  - sales tax amount
     *  - total amount
     *
     * @param   cartBorder  top border
     * @param   cart        cart object
     * @return              the formatted footer section
     */
    private String generateFooterSection(String cartBorder, Cart cart) {
        String footer = "";

        double subtotal = cart.getSubtotal();
        double salesTaxInDollarAmt = subtotal * cart.getSalesTaxRate();

        String subtotalFmt = String.format("$%.2f", subtotal),
                salesTaxFmt = String.format("$%.2f", salesTaxInDollarAmt),
                totalFmt = String.format("$%.2f", cart.getTotal());

        for (int footerLCtner = 0; footerLCtner < 7; footerLCtner++) {
            if (footerLCtner == 0 || footerLCtner == 2 || footerLCtner == 5) {
                footer += "*".concat(" ".repeat(148)).concat("*\n");    // empty line
            } else if (footerLCtner == 1 || footerLCtner == 3 || footerLCtner == 4) {
                footer += switch(footerLCtner) {
                    case 1 -> generateTwoLabelLine(subtotalLabel, subtotalFmt,32, 35);
                    case 3 -> generateTwoLabelLine(salesTaxLabel, salesTaxFmt, 32, 35);
                    case 4 -> generateTwoLabelLine(totalLabel, totalFmt, 32, 35);
                    default -> "";
                };
            } else {
                footer += String.format("%s\n", cartBorder);
            }
        }
        return footer;
    }

    /**
     * Print out the entire cart view
     *
     * This function prints out the entire cart view, which composes of the header section, body section, and footer section
     *
     * @param   currentCart     hold the accumulated quantity of each food item that the customer orders.
     * @param   cart            cart object
     */
    private void printCartView(List<CartForm> currentCart, Cart cart) {
        String view = "";
        String cartBorder = "*".repeat(borderLen);
        String divider = "*"
                .concat(" ".repeat(borderLen-146))
                .concat("-".repeat(borderLen-10))
                .concat(" ".repeat(borderLen-146))
                .concat("*\n");
        String emptyL = "*".concat(" ".repeat(148)).concat("*\n");

        view = generateHeaderSection(cartBorder) + divider + emptyL + generateBodySection(currentCart) + generateFooterSection(cartBorder, cart);
        System.out.print(view);
    }
}
