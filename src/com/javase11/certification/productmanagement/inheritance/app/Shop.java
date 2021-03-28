/*
 * Copyright (c) 2021. Oracle
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *  
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *  
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.inheritance.app;

import static com.javase11.certification.productmanagement.inheritance.data.ProductManager.createProduct;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.javase11.certification.productmanagement.inheritance.data.Product;
import com.javase11.certification.productmanagement.inheritance.data.Rating;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
	public static void main(final String[] args) {
		final Product p1 = createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.THREE_STAR);
		final Product p2 = createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
		final Product p3 = createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR,
				LocalDate.now().plusDays(2));
		final Product p4 = createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now());
		final Product p5 = p3.applyRating(Rating.THREE_STAR);
		final Product p6 = createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		final Product p7 = createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR,
				LocalDate.now().plusDays(2));
		final Product p8 = p4.applyRating(Rating.FIVE_STAR);
		final Product p9 = p1.applyRating(Rating.TWO_STAR);

		System.out.println(p5.equals(p6));
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		System.out.println(p6);
		System.out.println(p7);
		System.out.println(p8);
		System.out.println(p9);
	}
}
