/*
 * Copyright (c) 2021. Oracle
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *  
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *  
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.arrays.app;

import java.math.BigDecimal;
import java.util.Locale;

import com.javase11.certification.productmanagement.arrays.data.Product;
import com.javase11.certification.productmanagement.arrays.data.ProductManager;
import com.javase11.certification.productmanagement.arrays.data.Rating;

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
		System.out.println(p1);
		pm.printProductReport();
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
		p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
		p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
		p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
		p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
		pm.printProductReport();
	}
}
