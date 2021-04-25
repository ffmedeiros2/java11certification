/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.collections.data;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * @author oracle
 * @version 4.0
 */
public class ProductManager {
    private final Charset utf8Charset = Charset.forName("UTF-8");
    private final DateTimeFormatter dateFormat;
    private final NumberFormat moneyFormat;
    private final ResourceBundle resources = ResourceBundle.getBundle("com/javase11/certification/productmanagement/collections/data/resources");
    private final Map<Product, List<Review>> products = new HashMap<>();

    public ProductManager(final Locale locale) {
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating,
                                 final LocalDate bestBefore) {
        final Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) {
        final Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product findProduct(final int id) {
        Product result = null;
        for (final Product product : products.keySet()) {
            if (product.getId() == id) {
                result = product;
                break;
            }
        }
        return result;
    }

    public void printProductReport(final Product product) {
        final StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("product"), product.getName(),
                moneyFormat.format(product.getPrice()), product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        txt.append("\n");

        final List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        for (final Review review : reviews) {
            txt.append(MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
                    review.getComments()));
            txt.append("\n");
        }

        if (reviews.isEmpty()) {
            txt.append(resources.getString("no.reviews"));
            txt.append("\n");
        }

        try {
            final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
            out.println(txt);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void printProductReport(final int id) {
        printProductReport(findProduct(id));
    }

    public Product reviewProduct(final Product product, final Rating rating, final String comments) {
        final List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));

        int sum = 0;
        for (final Review review : reviews) {
            sum += review.getRating().ordinal();
        }

        final Product newProduct = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
        products.put(newProduct, reviews);

        return newProduct;
    }

    public Product reviewProduct(final int id, final Rating rating, final String comments) {
        return reviewProduct(findProduct(id), rating, comments);
    }
}
