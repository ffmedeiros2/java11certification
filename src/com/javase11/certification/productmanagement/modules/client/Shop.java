/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.modules.client;

import com.javase11.certification.productmanagement.modules.data.Product;
import com.javase11.certification.productmanagement.modules.data.Rating;
import com.javase11.certification.productmanagement.modules.data.Review;
import com.javase11.certification.productmanagement.modules.file.service.ProductFileManager;
import com.javase11.certification.productmanagement.modules.service.ProductManager;
import com.javase11.certification.productmanagement.modules.service.ProductManagerException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
    private static final Logger logger = Logger.getLogger(Shop.class.getName());
    private static final Charset utf8Charset = Charset.forName("UTF-8");

    public static void main(final String[] args) {

        try {
            final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
            final ResourceFormatter formatter = ResourceFormatter.getResourceFormatter("pt-BR");
            final ProductManager pm = new ProductFileManager();
            pm.createProduct(164, "Kombucha", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
            pm.reviewProduct(164, Rating.FIVE_STAR, "Perfect");
            pm.reviewProduct(164, Rating.FOUR_STAR, "Fine tea");
            pm.reviewProduct(164, Rating.FOUR_STAR, "This is not tea");
            pm.reviewProduct(164, Rating.TWO_STAR, "Looks like tea, but is it?");

            final Predicate<Product> lessThan2 = p -> p.getPrice().floatValue() < 2;
            pm.findProducts(lessThan2)
              .stream()
              .forEach(product -> out.println(formatter.formatProduct(product)));

            final Product product = pm.findProduct(101);
            final List<Review> reviews = pm.findReviews(101);


            out.println(formatter.formatProduct(product));
            reviews.forEach(review -> out.println(formatter.formatReview(review)));
            printFile(formatter.formatProductReport(product, reviews), Path.of(formatter.formatData("report", 101)));
        } catch (final ProductManagerException | UnsupportedEncodingException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    static void printFile(final String output, final Path filePath) {
        try (final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(Files.newOutputStream(filePath, StandardOpenOption.CREATE), "UTF-8"))) {
            out.append(output);
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Error writing data " + e.getMessage(), e);
        }
    }
}
