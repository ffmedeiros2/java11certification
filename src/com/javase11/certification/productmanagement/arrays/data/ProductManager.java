/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.arrays.data;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author oracle
 * @version 4.0
 */
public class ProductManager {
    private final Charset utf8Charset = Charset.forName("UTF-8");
    private final DateTimeFormatter dateFormat;
    private final NumberFormat moneyFormat;
    private final ResourceBundle resources = ResourceBundle.getBundle("com/javase11/certification/productmanagement/arrays/data/resources");
    private Product product;
    private Review[] reviews = new Review[5];

    public ProductManager(final Locale locale) {
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public void printProductReport() throws UnsupportedEncodingException {
        final StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("product"), product.getName(),
                moneyFormat.format(product.getPrice()), product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        txt.append("\n");
        for (final Review review : reviews) {
            if (review == null) {
                break;
            }
            txt.append(MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
                    review.getComments()));
            txt.append("\n");
        }
        if (reviews[0] == null) {
            txt.append(resources.getString("no.reviews"));
            txt.append("\n");
        }
        final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
        out.println(txt);
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating,
                                 final LocalDate bestBefore) {
        product = new Food(id, name, price, rating, bestBefore);
        return product;
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) {
        product = new Drink(id, name, price, rating);
        return product;
    }

    public Product reviewProduct(final Product product, final Rating rating, final String comments) {
        if (reviews[reviews.length - 1] != null) {
            reviews = Arrays.copyOf(reviews, reviews.length + 5);
        }

        int sum = 0;
        int i = 0;
        boolean reviewed = false;
        while (i < reviews.length && !reviewed) {
            if (reviews[i] == null) {
                reviews[i] = new Review(rating, comments);
                reviewed = true;
            }
            sum += reviews[i].getRating().ordinal();
            i++;
        }
        this.product = product.applyRating(Rateable.convert(Math.round((float) sum / i)));
        return this.product;
    }
}
