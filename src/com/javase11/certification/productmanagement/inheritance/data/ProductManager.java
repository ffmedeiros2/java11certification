/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.inheritance.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductManager {
	public static Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating,
			final LocalDate bestBefore) {
		return new Food(id, name, price, rating, bestBefore);
	}

	public static Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) {
		return new Drink(id, name, price, rating);
	}
}
