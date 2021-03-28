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
import java.time.LocalDate;

public class Food extends Product {
	private final LocalDate bestBefore;

	Food(final int id, final String name, final BigDecimal price, final Rating rating, final LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;
	}

	@Override
	public Product applyRating(final Rating newRating) {
		return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
	}

	@Override
	public BigDecimal getDiscount() {
		return bestBefore.isEqual(LocalDate.now()) ? super.getDiscount() : BigDecimal.ZERO;
	}

	/**
	 * Get the value of best before date for the product
	 * 
	 * @return the the value of bestBefore
	 */
	@Override
	public LocalDate getBestBefore() {
		return bestBefore;
	}

}
