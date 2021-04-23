/*
 * Copyright (c) 2021. Oracle
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *  
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *  
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.javaio.app;

import com.javase11.certification.productmanagement.javaio.data.ProductManager;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
	public static void main(final String[] args) {
		final ProductManager pm = new ProductManager("pt-BR");

		// pm.printProductReport(101);
		//
		// pm.createProduct(164, "Kombucha", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now().plusDays(3));
		// pm.reviewProduct(164, Rating.FIVE_STAR, "Perfect");
		// pm.reviewProduct(164, Rating.FOUR_STAR, "Fine tea");
		// pm.reviewProduct(164, Rating.FOUR_STAR, "This is not tea");
		// pm.reviewProduct(164, Rating.TWO_STAR, "Looks like tea, but is it?");
		// pm.printProductReport(164);
		//
		// final Predicate<Product> lessThan2 = p -> p.getPrice().floatValue() < 2;
		// final Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
		// final Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
		// pm.printProducts(lessThan2, ratingSorter.thenComparing(priceSorter));
		// pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + '\t' + discount));
	}
}
