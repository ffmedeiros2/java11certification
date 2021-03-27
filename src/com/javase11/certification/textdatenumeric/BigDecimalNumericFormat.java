package com.javase11.certification.textdatenumeric;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalNumericFormat {

    public static void main(String[] args) {
        double price = 1.85;
        double rate = 0.065;
        price -= price*rate;
        System.out.println(price);
        System.out.println(Math.round(price*100)/100.0);

        BigDecimal priceBig = BigDecimal.valueOf(1.85);
        BigDecimal rateBig = BigDecimal.valueOf(0.065);
        priceBig = priceBig.subtract(priceBig.multiply(rateBig));
        System.out.println(priceBig);
        System.out.println(priceBig.setScale(2, RoundingMode.HALF_UP));

        Locale locale = Locale.FRANCE;
        NumberFormat currentFormat = NumberFormat.getCurrencyInstance(locale);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
        percentFormat.setMaximumFractionDigits(2);
        System.out.println(currentFormat.format(priceBig));
        System.out.println(percentFormat.format(rateBig));
    }

}
