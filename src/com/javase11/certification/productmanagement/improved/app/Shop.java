/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.improved.app;

import com.javase11.certification.productmanagement.improved.data.Product;
import com.javase11.certification.productmanagement.improved.data.Rating;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
    public static void main(final String[] args) {
        final Charset utf8Charset = Charset.forName("UTF-8");
        final Product p1 = new Product(101, "Tea", BigDecimal.valueOf(1.99));
        final Product p2 = new Product(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        final Product p3 = new Product(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR);
        final Product p4 = new Product();
        final Product p5 = p3.applyRating(Rating.THREE_STAR);

        try {
            final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
            out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount() + " "
                    + p1.getRating().getStars());
            out.println(p2.getId() + " " + p2.getName() + " " + p2.getPrice() + " " + p2.getDiscount() + " "
                    + p2.getRating().getStars());
            out.println(p3.getId() + " " + p3.getName() + " " + p3.getPrice() + " " + p3.getDiscount() + " "
                    + p3.getRating().getStars());
            out.println(p4.getId() + " " + p4.getName() + " " + p4.getPrice() + " " + p4.getDiscount() + " "
                    + p4.getRating().getStars());
            out.println(p5.getId() + " " + p5.getName() + " " + p5.getPrice() + " " + p5.getDiscount() + " "
                    + p5.getRating().getStars());
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
