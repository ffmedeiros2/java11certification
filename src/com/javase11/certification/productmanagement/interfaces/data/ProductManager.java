/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.interfaces.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author oracle
 * @version 4.0
 */
public class ProductManager {
	private final DateTimeFormatter dateFormat;
	private final NumberFormat moneyFormat;
	private final ResourceBundle resources;
	private Product product;
	private Review review;

	public ProductManager(final Locale locale) {
		resources = ResourceBundle.getBundle("com/javase11/certification/productmanagement/interfaces/data/resources",
				locale);
		dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
		moneyFormat = NumberFormat.getCurrencyInstance(locale);
	}

	public void printProductReport() {
		final StringBuilder txt = new StringBuilder();
		txt.append(MessageFormat.format(resources.getString("product"), product.getName(),
				moneyFormat.format(product.getPrice()), product.getRating(),
				dateFormat.format(product.getBestBefore())));
		txt.append("\n");
		if (review != null) {
			txt.append(MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
					review.getComments()));
		} else {
			txt.append(resources.getString("no.reviews"));
		}
		System.out.println(txt);
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
		review = new Review(rating, comments);
		this.product = product.applyRating(rating);
		return this.product;
	}
}
