/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.basic.app;

import java.math.BigDecimal;

import com.javase11.certification.productmanagement.basic.data.Product;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
	public static void main(final String[] args) {
		final Product p1 = new Product();
		p1.setId(101);
		p1.setName("Tea");
		p1.setPrice(BigDecimal.valueOf(1.99));
		System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount());
	}
}
