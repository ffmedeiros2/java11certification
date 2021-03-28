/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.basic.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@code Product} class represents properties and behaviors of product objects in the Product Management System. <br>
 * Each product has an id, name, and price. <br>
 * Each product can have a discount, calculated based on a {@link Product#DISCOUNT_RATE discount rate}
 *
 * @author oracle
 * @version 4.0
 */
public class Product {
	/**
	 * A constant that defined a {@link java.math.BigDecimal BigDecimal} value of the discount rate <br>
	 * Discount rate is 10%
	 */
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	private int id;
	private String name;
	private BigDecimal price;

	/**
	 * Calculates discount based on a product price and {@link Product#DISCOUNT_RATE discount rate}
	 *
	 * @return a {@link java.math.BigDecimal BigDecimal} value of the discount
	 */
	public BigDecimal getDiscount() {
		return this.price.multiply(DISCOUNT_RATE).setScale(1, RoundingMode.HALF_UP);
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
