/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.interfaces.app;

import com.javase11.certification.productmanagement.interfaces.data.Product;
import com.javase11.certification.productmanagement.interfaces.data.ProductManager;
import com.javase11.certification.productmanagement.interfaces.data.Rating;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
    public static void main(final String[] args) {
        final ProductManager pm = new ProductManager(new Locale("pt", "BR"));
        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.printProductReport();
        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
        pm.printProductReport();
        // final Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        // final Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR,
        // LocalDate.now().plusDays(2));
        // final Product p4 = pm.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR,
        // LocalDate.now());
        // final Product p5 = p3.applyRating(Rating.THREE_STAR);
        // final Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
        // final Product p7 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR,
        // LocalDate.now().plusDays(2));
        // final Product p8 = p4.applyRating(Rating.FIVE_STAR);
        // final Product p9 = p1.applyRating(Rating.TWO_STAR);
    }
}
