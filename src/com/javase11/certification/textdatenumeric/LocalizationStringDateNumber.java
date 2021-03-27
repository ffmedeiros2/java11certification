package com.javase11.certification.textdatenumeric;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationStringDateNumber {

    public static void main(String[] args) {
        // localização de números
//        Locale br = new Locale("pt", "BR");
        Locale enUS = Locale.US;


        Double salario = 3200.0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(enUS);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(enUS);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", enUS);
        System.out.println(currencyFormat.format(salario));


        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", enUS);

        Product p = new Product("Cookie", 2.99, 4, LocalDate.of(2019, 4, 1));
        String name = p.name;
        String price = currencyFormat.format(p.price);
        String quantity = numberFormat.format(p.quantity);
        String bestBefore = dateFormat.format(p.bestBefore);


        String productPattern = bundle.getString("product");
        String message = MessageFormat.format(productPattern, name, price, quantity, bestBefore);

        System.out.println(message);

    }

    static class Product {
        String name;
        Double price;
        Integer quantity;
        LocalDate bestBefore;

        public Product(String name, Double price, Integer quantity, LocalDate bestBefore) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.bestBefore = bestBefore;
        }
    }
}
