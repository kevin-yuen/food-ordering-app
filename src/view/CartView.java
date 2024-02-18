package view;

import component.Cart;
import component.CartForm;

import java.lang.reflect.Field;
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

    public void determineCartView(List<CartForm> currentCart) {
        if (currentCart.size() == 0) {
            printNoItemView();
        }
        else {
            printItemView(currentCart);
        }
    }

    public void printNoItemView() {
        System.out.println("No item in cart.");
    }

    private String generateOneLabelLine(String content) {
        int numOfSpRemain = borderLen - content.length() - 2;   // subtract 2 due to * on both sides
        int numOfSpBeg = numOfSpRemain / 2, numOfSpEnd = numOfSpRemain - numOfSpBeg;

        return "*".concat(" ".repeat(numOfSpBeg)).concat(content).concat(" ".repeat(numOfSpEnd)).concat("*\n");
    }

    private String generateTwoLabelLine(String content1, String content2, int numOfSpToFillBeg, int numOfSpToFillEnd) {
        //int numOfSpToFillBeg = 32, numOfSpToFillEnd = 35;
        int numOfSpRemain = borderLen - (content1.length() + content2.length()) - 2;    // subtract 2 due to * on both sides
        int numOfSpToFillBtw = numOfSpRemain - numOfSpToFillBeg - numOfSpToFillEnd;

        return "*".concat(" ".repeat(numOfSpToFillBeg)).concat(content1).concat(" ".repeat(numOfSpToFillBtw)).concat(content2).concat(" ".repeat(numOfSpToFillEnd)).concat("*\n");
    }

    private String generateHeaderSection(String cartBorder) {
        String header = "";
        int cartBorderLen = cartBorder.length()-1;  // due to "\n" from user input, recalculate cartBorder length

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"),
                timeFormat = new SimpleDateFormat("h:mm a");
        String currentDt = dateFormat.format(currentDate), currentTime = timeFormat.format(currentDate);

        for (int headerLCtner = 0; headerLCtner < 10; headerLCtner++) {
            if (headerLCtner == 0) {
                //header += cartBorder;
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
                    for (var topping: toppings) {
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

    private String generateFooterSection(String cartBorder) {
        String footer = "";

        double subtotal = Cart.getSubtotal();
        double salesTaxInDollarAmt = subtotal * Cart.getSalesTaxRate();

        String subtotalFmt = String.format("$%.2f", subtotal),
                salesTaxFmt = String.format("$%.2f", salesTaxInDollarAmt),
                totalFmt = String.format("$%.2f", Cart.getTotal());

        for (int footerLCtner = 0; footerLCtner < 7; footerLCtner++) {
            if (footerLCtner == 0 || footerLCtner == 2 || footerLCtner == 5) {
                footer += "*".concat(" ".repeat(148)).concat("*\n");    // empty line
            }
            else if (footerLCtner == 1 || footerLCtner == 3 || footerLCtner == 4) {
                footer += switch(footerLCtner) {
                    case 1 -> generateTwoLabelLine(subtotalLabel, subtotalFmt,32, 35);
                    case 3 -> generateTwoLabelLine(salesTaxLabel, salesTaxFmt, 32, 35);
                    case 4 -> generateTwoLabelLine(totalLabel, totalFmt, 32, 35);
                    default -> "";
                };
            }
            else {
                footer += String.format("%s\n", cartBorder);
            }
        }
        return footer;
    }

    public void printItemView(List<CartForm> currentCart) {
        String view = "";
        String cartBorder = "*".repeat(borderLen);
        String divider = "*"
                .concat(" ".repeat(borderLen-146))
                .concat("-".repeat(borderLen-10))
                .concat(" ".repeat(borderLen-146))
                .concat("*\n");
        String emptyL = "*".concat(" ".repeat(148)).concat("*\n");

        view = generateHeaderSection(cartBorder) + divider + emptyL + generateBodySection(currentCart) + generateFooterSection(cartBorder);
        System.out.print(view);
    }
}
