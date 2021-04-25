/*
 * Copyright (c) 2021. Oracle
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *  
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *  
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.modules.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

/**
 * {@code Product} class represents properties and behaviors of product objects in the Product Management System. <br>
 * Each product has an id, name, and price. <br>
 * Each product can have a discount, calculated based on a {@link Product#DISCOUNT_RATE discount rate}
 *
 * @author oracle
 * @version 4.0
 */
public abstract class Product implements Rateable<Product>, Serializable {
	/**
	 * A constant that defined a {@link BigDecimal BigDecimal} value of the discount rate <br>
	 * Discount rate is 10%
	 */
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	private static final long serialVersionUID = 7760839199144950317L;
	private final int id;
	private final String name;
	private final BigDecimal price;
	private final Rating rating;

	Product(final int id, final String name, final BigDecimal price, final Rating rating) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}

	/**
	 * Calculates discount based on a product price and {@link Product#DISCOUNT_RATE discount rate}
	 *
	 * @return a {@link BigDecimal BigDecimal} value of the discount
	 */
	public BigDecimal getDiscount() {
		return this.price.multiply(DISCOUNT_RATE).setScale(1, RoundingMode.HALF_UP);
	}

	public int getId() {
		return id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	@Override
	public Rating getRating() {
		return rating;
	}

	/**
	 * Get the value of best before date for the product
	 *
	 * @return the the value of bestBefore
	 */
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		// if (o != null && getClass() == o.getClass()) {
		if (o instanceof Product) {
			final Product product = (Product) o;
			return id == product.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + price + ", " + getDiscount() + ", " + rating.getStars() + ", "
				+ getBestBefore();
	}
}
