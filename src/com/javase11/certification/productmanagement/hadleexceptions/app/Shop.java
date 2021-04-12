/*
 * Copyright (c) 2021. Oracle
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *  
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *  
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.hadleexceptions.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

import com.javase11.certification.productmanagement.hadleexceptions.data.Product;
import com.javase11.certification.productmanagement.hadleexceptions.data.ProductManager;
import com.javase11.certification.productmanagement.hadleexceptions.data.Rating;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
	public static void main(final String[] args) {
		final ProductManager pm = new ProductManager("pt-BR");
		// pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
		pm.parseProduct("D,101,Tea,1.99,0,2019-09-19");
		// pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
		// pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
		// pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
		// pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea");
		// pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
		pm.parseReview("101,4,Nice hot cup of tea");
		pm.parseReview("101,2,Rather weak tea");
		pm.parseReview("101,4,Fine tea");
		pm.parseReview("101,5,Perfect tea");
		pm.parseReview("101,3,Just add some lemon");
		pm.printProductReport(101);

		pm.changeLocale("en-US");

		pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
		pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
		pm.reviewProduct(102, Rating.ONE_STAR, "Where is theb milk?!");
		pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with ten spoons of sugar");
		// pm.printProductReport(102);

		// pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now().plusDays(2));
		pm.parseProduct("F,103,Cake,3.99,0,2019-09-19");
		pm.reviewProduct(103, Rating.FIVE_STAR, "Very nice cake");
		pm.reviewProduct(103, Rating.FOUR_STAR, "It's good, but I've expected mor chocolate");
		pm.reviewProduct(103, Rating.FIVE_STAR, "This cake is perfect");
		pm.printProductReport(103);

		pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), Rating.NOT_RATED, LocalDate.now());
		pm.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
		pm.reviewProduct(104, Rating.THREE_STAR, "Ok");
		// pm.printProductReport(104);

		pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.50), Rating.FIVE_STAR);
		pm.reviewProduct(105, Rating.FOUR_STAR, "Tasty!");
		pm.reviewProduct(105, Rating.FOUR_STAR, "Not bad at all");
		// pm.printProductReport(105);

		pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.50), Rating.FIVE_STAR, LocalDate.now().plusDays(3));
		pm.reviewProduct(106, Rating.TWO_STAR, "Too sweat");
		pm.reviewProduct(106, Rating.THREE_STAR, "Better then cookie");
		pm.reviewProduct(106, Rating.TWO_STAR, "Too bitter");
		pm.reviewProduct(106, Rating.ONE_STAR, "I don't get it!");
		// pm.printProductReport(106);

		final Predicate<Product> lessThan2 = p -> p.getPrice().floatValue() < 2;
		final Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
		final Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
		// pm.printProducts(lessThan2, ratingSorter.thenComparing(priceSorter));
		// pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + '\t' + discount));
		// pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());
	}
}
